package org.test.commands;

import org.eclipse.jgit.api.Git;
import org.test.SmartCommitInitializer;
import org.test.SmartCommitService;
import org.test.singletons.Initializer;
import picocli.CommandLine;

@CommandLine.Command(
        name = "--init",
        version = "1.0.0",
        description = "Initializes the smart commit configuration",
        header = "Initializer Command",
        mixinStandardHelpOptions = true
)

public class CliInitializer implements Runnable{
    public void run(){
        System.out.println("Initializing smart commit...");
        new SmartCommitInitializer().initConfig();
        System.out.println("Successfully initialized smart commit");
    }
}
