package org.test;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.patch.FileHeader;
import org.test.diffsummary.DiffSummary;
import org.test.exceptions.GitDiffException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DiffEntryParser {
    private final Git git;
    private static final Logger log = Logger.getLogger(DiffEntryParser.class.getName());

    public DiffEntryParser(Git git){
        this.git = git;
    }

    //Gets the raw patch content of git diffs. Mainly for entries to provide more context
    public List<String> getRawDiffContent(List<DiffEntry> diffEntries){
        List<String> patches = new ArrayList<>();
        String patch = "";

        try(ByteArrayOutputStream out = new ByteArrayOutputStream();
            DiffFormatter diffFormatter = new DiffFormatter(out)){
            modifyDiffFormatter(diffFormatter);

            //Format the diff entry to get all the changes from the entry
            for(DiffEntry entry: diffEntries){
                diffFormatter.format(entry);
                patch = out.toString(StandardCharsets.UTF_8);
                patches.add(patch);
                out.reset();
            }

            return patches;
        }catch (IOException e){
            log.severe("An IO error occurred while trying to read diff entries");
            throw new GitDiffException("An IO error occurred while trying to read diff entries");
        }
    }


    //Summarizes diff entries to look more like a git diff --stat command (in object form)
    public List<DiffSummary> getSummarizedDiffContent(List<DiffEntry> diffEntries){
        List<DiffSummary> summaries = new ArrayList<>();
        DiffSummary summary;
        FileHeader fh;
        EditList el;

        try(ByteArrayOutputStream out = new ByteArrayOutputStream();
            DiffFormatter formatter = new DiffFormatter(out)
        ){
            modifyDiffFormatter(formatter);

            //Summarize the diff entries
            for (DiffEntry entry: diffEntries){
                formatter.format(entry);
                fh = formatter.toFileHeader(entry);
                el = fh != null ? fh.toEditList() : new EditList();
                int linesAdded = 0;
                int linesDeleted = 0;

                if(el.isEmpty())continue;

                for (Edit e: el){
                    Edit.Type type = e.getType();

                    switch (type){
                        case INSERT -> linesAdded += (e.getEndB() - e.getBeginB());
                        case DELETE -> linesDeleted += (e.getEndA() - e.getBeginA());
                        case REPLACE -> {
                            linesAdded += (e.getEndB() - e.getBeginB());
                            linesDeleted += (e.getEndA() - e.getBeginA());
                        }
                    }
                }

                summary = DiffSummary
                        .builder()
                        .changeType(entry.getChangeType().name())
                        .newPath(entry.getNewPath())
                        .oldPath(entry.getOldPath())
                        .linesDeleted(linesDeleted)
                        .linesAdded(linesAdded)
                        .build();

                summaries.add(summary);
            }

            log.info(String.format("Successfully processed %s summaries", summaries.size()));
            return summaries;

        }catch (IOException e){
            log.severe("An IO error occurred while trying to generate summaries for diff entries");
            throw new GitDiffException("An IO error occurred while trying to generate summaries for diff entries");
        }
    }


    private void modifyDiffFormatter(DiffFormatter diffFormatter){
       diffFormatter.setRepository(git.getRepository());
       diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
       diffFormatter.setDetectRenames(true);
    }
}
