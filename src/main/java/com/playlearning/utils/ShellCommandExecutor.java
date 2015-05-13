package com.playlearning.utils;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShellCommandExecutor {

    private static List<String> getResultStrings(InputStream ins) throws Exception {
        String line;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        List<String> strings = new LinkedList<String>();
        while ((line = in.readLine()) != null) {
            strings.add(line);
        }
        return strings;
    }

    public static String runCode(File file) throws Exception {
        Process pro;
        pro = Runtime.getRuntime().exec("javac " + file);
        pro.waitFor();
        if (pro.exitValue() == 0) {
            pro = Runtime.getRuntime().exec("java -cp " + file.getParentFile().getAbsolutePath() + " " + file.getName().replace(".java", ""));
            pro.waitFor();
        } else {
            throw new Exception();
        }

        StringBuilder stringBuilder = new StringBuilder();
        for(String s : getResultStrings(pro.getInputStream())) {
            stringBuilder.append(s + "\n");
        }
        return stringBuilder.toString();
    }
}
