package org.test;


import com.google.api.client.util.SecurityUtils;
import io.github.cdimascio.dotenv.Dotenv;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.ChainingCredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.test.exceptions.GitAddException;
import org.test.exceptions.GitCommitException;
import org.test.exceptions.GitDiffException;
import org.test.exceptions.GitPushException;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class GitUtils {
    private final Git git;
    private final static Logger log = Logger.getLogger(GitUtils.class.getName());

    public GitUtils(Git git) {
        this.git = git;
    }

    /**
     * Gets all the diff entries
     * @param excludedFileExtensions File extensions to exclude from the diff
     * @return A list of diff entries
     * */
    @ExcludeFileExtensions
    public List<DiffEntry> gitDiffEntries(Set<String> excludedFileExtensions){
        DiffCommand diffCommand = git.diff().setCached(true);

        try {
            List<DiffEntry> entries = diffCommand.call();
            log.info(String.format("Found %s diff entries for this commit.", entries.size()));

            //Filter out the excluded files from the diff
            return entries.stream().filter(e -> {
                String extension= "";
                String path = e.getNewPath();
                int lastDotIndex = path.lastIndexOf(".");

                if(lastDotIndex > 0){
                    extension = path.substring(lastDotIndex);
                    log.info(extension);
                }
                return !excludedFileExtensions.contains(extension);
            }).toList();

        } catch (GitAPIException e) {
            log.severe("A Git API error occurred while trying to get git diff for this commit.");
            throw new GitDiffException("A Git API error occurred while trying to get git diff for this commit.", e);
        }
    }

    /**
     * Adds files to staging area
     * @return The temporary staging area
     * */
    public DirCache gitAdd(){
        AddCommand addCommand = git.add().addFilepattern(".").setUpdate(true);

        try{
            DirCache cache = addCommand.call();
            log.info("Successfully added all files: {} to staging area for commit");
            return cache;
        }catch (GitAPIException e){
            log.severe("A Git API error occurred while trying to add all file under %s to the staging area for commit");
            throw new GitAddException("A Git API error occurred while trying to add all file under %s to the staging area for commit", e);
        }
    }

    /**
     * Commits files from the staging area to the repo
     * @return A temporary revision pool
     * */
    public RevCommit gitCommit(String commitMessage){
        CommitCommand commitCommand = git.commit().setMessage(commitMessage);

        try{
            RevCommit revCommit = commitCommand.call();
            log.info("Successfully committed changes from the staging area to the repository");
            return revCommit;
        }catch (GitAPIException e){
            log.severe("A Git API error occurred while trying to commit changes to the repository");
            throw new GitCommitException("A Git API error occurred while trying to commit changes to the repository", e);
        }
    }

    public void gitPush(){
        PushCommand pushCommand = git.push();
        pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(Dotenv.load().get("PAT_TOKEN"), ""));

////        pushCommand.setRefSpecs(new RefSpec("refs/heads/main:refs/heads/main"));
        String remoteRepo = pushCommand.getRemote();
//        if(remoteRepo == null || remoteRepo.isEmpty()){
//            log.severe("Failed to find a remote git repository to push to. Returning");
//            throw new GitPushException("Failed to find a remote git repository to push changes to.");
//        }

        try{
            pushCommand.call();
            log.info(String.format("Successfully pushed changes to remote git repository. Repository: {%s}", remoteRepo));
        }catch (GitAPIException e){
            log.severe(String.format("A Git API error occurred while trying to push changes to remote repository. Repository: {%s}", remoteRepo));
            throw new GitPushException(String.format("A Git API error occurred while trying to push changes to remote repository. Repository: {%s}", remoteRepo), e);
        }
    }




}
