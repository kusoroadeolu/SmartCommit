package org.test.commands;

import picocli.CommandLine;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@CommandLine.Command(
        version = "1.0.0",
        subcommands = {CliSuggest.class, CliDirectRun.class, CliInitializer.class},
        mixinStandardHelpOptions = true,
        commandListHeading = "Subcommands are: \n",
        description = "Initializes the smart commit service, creates a config file and ensure the directory is a git repo"
)
public class CliRunner implements Runnable{
    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        Logger.getLogger("").setLevel(Level.OFF);

        int status = new CommandLine(CliRunner.class).execute(args);
        System.exit(status);
    }

    public void run(){
        //THIS WILL PRINT OUT THE READ ME LOL
        System.out.println();
    }

}