package org.test;

import picocli.CommandLine;

@CommandLine.Command(
        name = "Hello [name]",
        description = "It prints Hello [name]"
)
public class HelloNameCommand implements Runnable{

    @CommandLine.Parameters(index = "0", description = "The name to print")
    public String name;

    public static void main(String[] args){
        new CommandLine(HelloNameCommand.class).execute(args);
    }

    public void run(){
        System.out.println("Hello " + name);
    }
}
