package org.test;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.test.exceptions.GitDiffException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DiffEntryParser {
    private final GeminiClient geminiClient;
    private final GitUtils gitUtils;
    private final Git git;
    private static final Logger log = Logger.getLogger(DiffEntryParser.class.getName());

    public DiffEntryParser(GitUtils gitUtils, Git git){
        this.geminiClient = GeminiClient.getGeminiClient();
        this.gitUtils = gitUtils;
        this.git = git;
    }


    public List<String> getRawDiffContent(List<DiffEntry> diffEntries){
        List<String> patches = new ArrayList<>();
        String patch = "";

        try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
            DiffFormatter diffFormatter = new DiffFormatter(out);
            modifyDiffFormatter(diffFormatter);

            //Format the diff entry to get all the changes from the entry
            for(DiffEntry entry: diffEntries){
                diffFormatter.format(entry);
                patch = out.toString();
                patches.add(patch);
                out.reset();
            }

            //Close the diff formatter and return
            diffFormatter.close();
            return patches;
        }catch (IOException e){
            log.severe("An IO error occurred while trying to read diff entries");
            throw new GitDiffException("An IO error occurred while trying to read diff entries");
        }
    }

//    public List<DiffSummary> getSummarizedDiffContent(List<DiffEntry> diffEntries){
//        List<DiffSummary> summaries = new ArrayList<>();
//        try(BufferedOutputStream out = new BufferedOutputStream(new ByteArrayOutputStream())){
//            DiffFormatter formatter = new DiffFormatter(out);
//            modifyDiffFormatter(formatter);
//            DiffSummary summary = new DiffSummary();
//
//            for (DiffEntry entry: diffEntries){
//                summary.modifyDiffEntry(entry);
//                formatter.format(entry);
//                FileHeader fh = formatter.toFileHeader(entry);
//                //fh.get
//
//            }
//
//        }catch (IOException e){
//            log.severe("An IO error occurred while trying to read diff entries");
//            throw new GitDiffException("An IO error occurred while trying to read diff entries");
//        }
//    }


    private void modifyDiffFormatter(DiffFormatter diffFormatter){
       diffFormatter.setRepository(git.getRepository());
       diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
       diffFormatter.setDetectRenames(true);
    }
}
