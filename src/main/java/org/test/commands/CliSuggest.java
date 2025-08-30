package org.test.commands;

import org.test.SmartCommitService;
import picocli.CommandLine;

@CommandLine.Command(
        name = "suggest",
        version = "1.0.0",
        mixinStandardHelpOptions = true,
        header = "Suggest Command",
        aliases = {"s"},
        description = "This is a command to suggest a commit message",
        optionListHeading = "Options are: \n"
)
public class CliSuggest implements Runnable{

    @CommandLine.Option(
            names = {"-m", "--mode"},
            description = "Prints a commit message with more diff context \"detail\", otherwise defaults to a summary message with less diff context",
            paramLabel = "<commit mode>"
    )
    private String mode;

    public void run(){
        SmartCommitService sc = new SmartCommitService();
        String message = sc.suggestMessage(mode);
        System.out.println(message);
    }
}