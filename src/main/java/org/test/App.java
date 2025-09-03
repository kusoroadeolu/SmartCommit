package org.test;

import java.io.IOException;
import java.util.Arrays;

public class App{
    public static void main(String[] args) throws IOException {
        String[] words = {"a","b","ba","bca","bdca","bda"};
        Arrays.sort(words);
        System.out.println(Arrays.toString(words));
    }
}




