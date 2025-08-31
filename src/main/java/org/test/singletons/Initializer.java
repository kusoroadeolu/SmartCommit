package org.test.singletons;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.test.FileUtils;
import org.test.exceptions.RepositoryException;
import org.test.exceptions.SmartCommitException;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class Initializer {

    private static final String ROOT_DIR = System.getProperty("user.dir");
    private final static Logger log = Logger.getLogger(Initializer.class.getName());
    private final static Set<String> CACHED_EXCLUSIONS = new HashSet<>();

    public Git initGit(){
        Git git;
        try{
            git = Git.open(new File(ROOT_DIR));
            log.info(String.format("Git repo found in %s. Initializing program...", ROOT_DIR));
            return git;
        } catch (IOException e) {
            log.severe(String.format("Failed to find git repo in %s. Initializing git repo", ROOT_DIR));
            return createGitRepo();
            //throw new RepositoryNotFoundException(String.format("Failed to find git repo in: %s. Initialize a repo to continue", ROOT_DIR));
        }
    }

    private static Git createGitRepo(){
        try {
            return Git.init().setDirectory(new File(ROOT_DIR)).call();
        }catch (GitAPIException e){
            log.severe(String.format("Failed to initialize a git repo in directory: %s", ROOT_DIR));
            throw new RepositoryException(String.format("Failed to initialize a git repo in directory: %s", ROOT_DIR), e);
        }
    }


    private static class ProgramInitializerHelper{
        private static final Initializer INITIALIZER = new Initializer();
    }

    public static Initializer getInstance(){
        return ProgramInitializerHelper.INITIALIZER;
    }


}
