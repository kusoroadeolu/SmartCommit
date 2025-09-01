package org.test;


import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.test.exceptions.GitAddException;
import org.test.exceptions.GitCommitException;
import org.test.exceptions.GitDiffException;
import org.test.exceptions.GitPushException;

import java.io.IOException;
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
     * @param commitMessage The commit message
     * @param name The name of the author and committer
     * @param email The email of the author and committer
     * @return A temporary revision pool
     * */
    public RevCommit gitCommit(String commitMessage, String name, String email){
        CommitCommand commitCommand = git.commit().setMessage(commitMessage);

        if(name.isEmpty() && email.isEmpty()){
            PersonIdentWrapper personIdent = FileUtils.extractPersonIdent();
            name = personIdent.name();
            email = personIdent.email();
        }

        commitCommand.setAuthor(new PersonIdent(name, String.format("<%s>", email)));
        commitCommand.setCommitter(new PersonIdent(name, String.format("<%s>", email)));

        try{
            RevCommit revCommit = commitCommand.call();
            log.info("Successfully committed changes from the staging area to the repository");
            return revCommit;
        }catch (GitAPIException e){
            log.severe("A Git API error occurred while trying to commit changes to the repository");
            throw new GitCommitException("A Git API error occurred while trying to commit changes to the repository", e);
        }
    }

    //This will always push to the user's current working branch
    public void gitPush(){
        String patToken = FileUtils.extractPATToken();
        Repository repo = git.getRepository();
        PushCommand pushCommand = git.push()
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(patToken , ""));

        try{
            pushCommand.setRefSpecs(new RefSpec(repo.getBranch() + ":" + repo.getBranch()));
            pushCommand.call();
            log.info("Successfully pushed changes to remote git repository");
        }catch (GitAPIException e){
            log.severe("A Git API error occurred while trying to push changes to remote repository.");
            throw new GitPushException("A Git API error occurred while trying to push changes to remote repository", e);
        }catch (IOException e){
            log.severe("An IO error occurred while trying to get the user's current working branch.");
            throw new GitPushException("An IO error occurred while trying to get the user's current working branch", e);
        }
    }




}
