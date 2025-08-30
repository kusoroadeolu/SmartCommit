package org.test.commands;

import org.test.SmartCommitService;
import picocli.CommandLine;

@CommandLine.Command(
        name = "init",
        version = "1.0.0",
        aliases = {"init"},
        subcommands = {CliSuggest.class, CliDirectRun.class},
        mixinStandardHelpOptions = true,
        commandListHeading = "Subcommands are: \n",
        description = "Initializes the smart commit service, creates a config file and ensure the directory is a git repo"
)
public class CliRunner implements Runnable{

    public static void main(String[] args) {
        int status = new CommandLine(CliRunner.class).execute(args);
        System.exit(status);
    }

    public void run(){
        new SmartCommitService();
    }
}