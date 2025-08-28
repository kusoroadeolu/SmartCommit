package org.test;


import io.github.cdimascio.dotenv.Dotenv;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.internal.storage.dfs.DfsReader;
import org.eclipse.jgit.lib.ObjectReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class App {

    public static void main(String[] args) throws GitAPIException {
        Git git = ProgramInitializer.getInstance().initGit();

        List<DiffEntry> entries = git.diff().setCached(true).call().stream().toList();
        entries.forEach(e -> System.out.println(e.getNewPath()));

            try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                DiffFormatter df = new DiffFormatter(outputStream)
            ){
                df.setRepository(git.getRepository());
                df.setDetectRenames(true);
                df.setDiffComparator(RawTextComparator.DEFAULT);

                for (DiffEntry entry: entries){
                    outputStream.reset();
                    df.format(entry);
                    String patch = outputStream.toString(StandardCharsets.UTF_8);
                    System.out.println("PATCH: " + patch);

                }
            }catch (IOException e){

            }

    }
}

