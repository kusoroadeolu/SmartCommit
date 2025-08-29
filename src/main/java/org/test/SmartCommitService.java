package org.test;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.test.diffsummary.DiffSummary;
import org.test.exceptions.SmartCommitException;
import org.test.singletons.GeminiClient;
import org.test.singletons.Initializer;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class SmartCommitService {
    private final GitUtils gitUtils;
    private final DiffEntryParser parser;
    private final GeminiClient geminiClient;
    private static final Logger log = Logger.getLogger(SmartCommitService.class.getName());

    public SmartCommitService(){
        Git git = Initializer.getInstance().initGit();
        gitUtils = new GitUtils(git);
        parser = new DiffEntryParser(gitUtils, git);
        geminiClient = GeminiClient.getInstance();
    }

    //Suggests a commit message based on two modes -> {Summary, Detailed}
    public String suggestMessage(String mode){
        try{
            Set<String> excludedFileExtensions = FileUtils.extractExcludedExtensions();
            List<DiffEntry> entries = gitUtils.gitDiffEntries(excludedFileExtensions);

            if (mode.equals("detail")) {
                List<String> patches = parser.getRawDiffContent(entries);
                String patchString = DiffConverter.convertDiffPatches(patches);
                return geminiClient.generateMessage(patchString);
            }

            List<DiffSummary> summaries = parser.getSummarizedDiffContent(entries);
            String jsonString = DiffConverter.diffSummariesToJson(summaries);
            return geminiClient.generateMessage(jsonString);
        }catch (Exception e){
            log.info("A Git exception occurred while trying to suggest commit message");
            throw new SmartCommitException("A Git exception occurred while trying to suggest commit message", e);
        }

    }

    //Handles the basic git push flow(add -> commit -> push) with an automatic message(this will be optional later)
    public void directRun(String mode){
       try{
        gitUtils.gitAdd();

        String message = suggestMessage(mode);
        gitUtils.gitCommit(message);

        gitUtils.gitPush();
       }catch (Exception e){
           log.info("A Git exception occurred while trying to execute direct run");
           throw new SmartCommitException("A Git exception occurred while trying to execute direct run", e);
       }
    }

    public void directRunManualMessage(String commitMessage){
        try{
            gitUtils.gitAdd();
            gitUtils.gitCommit(commitMessage);
            gitUtils.gitPush();
        }catch (Exception e){
            log.info("A Git exception occurred while trying to execute direct run");
            throw new SmartCommitException("A Git exception occurred while trying to execute direct run", e);
        }
    }








}
