package org.test.commands;

import org.test.SmartCommitService;
import picocli.CommandLine;

@CommandLine.Command(
        header = "Direct Run Command",
        version = "1.0.0",
        name = "direct-run",
        mixinStandardHelpOptions = true,
        description = "Allows you to add, commit and push changes to your main branch(with or without a suggested commit message)",
        optionListHeading = "Options are: \n"
)
public class CliDirectRun implements Runnable{

    @CommandLine.Option(
            names = {"-m", "--mode"},
            description = "Prints a commit message with more diff context \"detail\", otherwise defaults to a summary message with less diff context",
            paramLabel = "<commit mode>"
    )
    private String mode = "";

    @CommandLine.Option(
            names = {"-msg", "--message"},
            description = "Your custom commit message",
            paramLabel = "<commit message>"
    )
    private String message;

    @CommandLine.Option(
            names = {"-n", "--name"},
            description = "The name of the committer and author",
            paramLabel = "<committer and author name>"
    )
    private String name = "";

    @CommandLine.Option(
            names = {"-e", "--email"},
            description = "The email of the committer and author",
            paramLabel = "<committer and author email>"
    )
    private String email = "";




    @Override
    public void run() {
        System.out.println("Executing direct run command...");
        SmartCommitService smartCommitService = new SmartCommitService();
        smartCommitService.directRun(mode.toLowerCase(), message, name, email);
        System.out.println("Successfully added, committed and pushed Git changes remote repository");


    }
}
