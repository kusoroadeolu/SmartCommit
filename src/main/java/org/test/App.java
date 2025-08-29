package org.test;


import org.eclipse.jgit.api.errors.GitAPIException;
import picocli.CommandLine;


public class App{

    public static void main(String[] args){
        SmartCommitService smartCommitService = new SmartCommitService();
        smartCommitService.directRun("detail");
//        System.out.println(message);
    }


}

