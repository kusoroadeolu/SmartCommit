package org.test;

import org.apache.commons.logging.Log;
import org.eclipse.jgit.api.Git;
import org.test.exceptions.SmartCommitException;
import org.test.singletons.Initializer;

import java.util.logging.Logger;

public class SmartCommitInitializer {

    private static final Logger log = Logger.getLogger(SmartCommitInitializer.class.getName());

    //Allows graceful close of git stream with try catch auto close
    public Git initConfig(){
        try(Git git = Initializer.getInstance().initGit()){
            FileUtils.createConfigFile();
            log.info("Successfully initialized smart-commit and created config file");
            return git;
        }catch (Exception e) {
            log.severe("An unexpected error occurred while trying to initialize config properties.");
            throw new SmartCommitException("An unexpected error occurred while trying to initialize config properties.", e);
        }
    }
}
