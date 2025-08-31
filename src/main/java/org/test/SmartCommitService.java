package org.test;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.test.diffsummary.DiffSummary;
import org.test.exceptions.SmartCommitException;
import org.test.singletons.GeminiClient;
import org.test.singletons.Initializer;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

public class SmartCommitService {
    private final GitUtils gitUtils;
    private final DiffEntryParser parser;
    private final GeminiClient geminiClient;
    private static final Logger log = Logger.getLogger(SmartCommitService.class.getName());

    public SmartCommitService(){
        Git git = new SmartCommitInitializer().initConfig();
        gitUtils = new GitUtils(git);
        parser = new DiffEntryParser(git);
        geminiClient = GeminiClient.getInstance();
    }

    //Suggests a commit message based on two modes -> {Summary, Detailed}
    public String suggestMessage(String mode){
        try{
            Set<String> excludedFileExtensions = FileUtils.extractExcludedExtensions();
            List<DiffEntry> entries = gitUtils.gitDiffEntries(excludedFileExtensions);

            if(entries == null || entries.isEmpty()){
                return "No diff entries found to generate a commit message";
            }

            if(mode == null || mode.isEmpty()){
                mode = FileUtils.extractCommitMode();
                return handleMode(mode, entries);
            }else{
                return handleMode(mode, entries);
            }

        }catch (Exception e){
            log.info("A Git exception occurred while trying to suggest commit message");
            throw new SmartCommitException("A Git exception occurred while trying to suggest commit message", e);
        }

    }

    //Handle how commit messages are suggested
    private String handleMode(String mode, List<DiffEntry> entries){
        return switch (mode){
            case "detail" -> {
                yield generateMessageWithDetailedContext(entries);
            }
            case "summary" -> generateMessageWithSummaryContext(entries);
            default -> {
                yield generateMessageWithSummaryContext(entries);
            }
        };
    }

    //Generates a commit message with more detailed context
    private String generateMessageWithDetailedContext(List<DiffEntry> entries){
        List<String> patches = parser.getRawDiffContent(entries);
        String patchString = DiffConverter.convertDiffPatches(patches);
        return geminiClient.generateMessage(patchString);
    }

    //Generates a commit message with summary context
    private String generateMessageWithSummaryContext(List<DiffEntry> entries){
        List<DiffSummary> summaries = parser.getSummarizedDiffContent(entries);
        String jsonString = DiffConverter.diffSummariesToJson(summaries);
        return geminiClient.generateMessage(jsonString);
    }

    //Handles the basic git push flow(add -> commit -> push) with an automatic message
    public void directRun(String mode, String message){
       try{
           gitUtils.gitAdd();
           if(Objects.equals(mode, "manual")){
               if(message == null || message.isEmpty()){
                   log.info("Messages in manual mode cannot be empty or null. Please write a valid message");
                   throw new SmartCommitException("Messages in manual mode cannot be empty or null. Please write a valid message");
               }
               gitUtils.gitCommit(message);
           }else{
               String suggestedMessage = suggestMessage(mode);
               gitUtils.gitCommit(suggestedMessage);
           }

           gitUtils.gitPush();
       }catch (Exception e){
           log.info("A Git exception occurred while trying to execute direct run");
           throw new SmartCommitException("A Git exception occurred while trying to execute direct run", e);
       }
    }
}
