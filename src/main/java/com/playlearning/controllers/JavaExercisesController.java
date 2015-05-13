package com.playlearning.controllers;

import com.playlearning.beans.Code;
import com.playlearning.utils.FileWriteUtils;
import com.playlearning.utils.ShellCommandExecutor;
import com.playlearning.utils.XmlParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Date;
import java.util.List;

@Controller
public class JavaExercisesController {
    public static final String FILE_EXTANTION = ".java";
    public static final String OUTPUT_TYPE = "output";
    public static final String COMPILERUN_TYPE = "compilerun";

    @RequestMapping(value = "/javaExercisesSection", params = {"subChapter"}, method = RequestMethod.GET)
    public String getJavaExercises(Model model, @RequestParam String subChapter) {
        model.addAttribute("view", "fragments/sections/javaExercisesSection");

        return "default";
    }

    @RequestMapping(value = "/doExerciseSection", params = {"exNumber"}, method = RequestMethod.GET)
    public String getDoExercise(Model model, @RequestParam String exNumber) {
        model.addAttribute("view", "fragments/sections/doExerciseSection");

        return "default";
    }

    @RequestMapping(value = "/processCode", params = {"exNumber"}, method = RequestMethod.POST)
    public String processCode(@ModelAttribute(value = "code") Code code, Model model, @RequestParam String exNumber,  HttpServletRequest request) throws Exception {
        String name = "";
        if(request.getUserPrincipal() != null) {
            name = request.getUserPrincipal().getName();
        }

        File file = createJavaFile(name, code);

        String result = "";
        boolean successfulRun = true;
        if (file != null) {
            try {
            result = ShellCommandExecutor.runCode(file);
            } catch (Exception e) {
                successfulRun = false;
            }
        }

        model.addAttribute("view", "fragments/sections/doExerciseSection");
        model.addAttribute("result", result);
        model.addAttribute("runSuccessful", successfulRun);

        FileWriteUtils.deleteDirectory(file);

        return "default";
    }

//    private boolean isSuccessful(Exercise exercise, File file, String result) {
//        if (exercise.getResultType().equals(OUTPUT_TYPE)) {
//            return result.replace("\n", "").toLowerCase().equals(exercise.getResult().replace("\n", "").toLowerCase());
//        } else if (exercise.getResultType().equals(COMPILERUN_TYPE)) {
//            Path path = Paths.get(file.getAbsolutePath());
//
//            List<String> strings = null;
//            try {
//                strings = Files.readAllLines(path, Charset.defaultCharset());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            for (String line : strings) {
//                if (line.contains(exercise.getResult())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private Exercise findExercise(String exNumber) {
//        for (Exercise exercise : exercises.getExercises()) {
//            if (exercise.getNumber().equals(exNumber)) {
//                return exercise;
//            }
//        }
//        return null;
//    }

    private File createJavaFile(String name, @ModelAttribute(value = "code") Code code) throws IOException {
        String className = code.getCodeToProcess().split("public class ")[1].split(" ")[0].replaceAll("\\{", "").trim();
        String directoryPath = name + File.separator + new Date().getTime();
        File directory = new File(directoryPath);
        directory.mkdirs();

        String fileNameWithEx = className + ".java";
        File file = new File(directory.getAbsolutePath() + File.separator + fileNameWithEx);
        file.createNewFile();

        String fileName = file.getName().replace(FILE_EXTANTION, "");

        String receivedCode = code.getCodeToProcess().replace(className, fileName);
        FileWriteUtils.writeToFile(receivedCode, file.getAbsolutePath());
        return file;
    }
}
