package controllers;

import beans.Code;
import utils.FileWriteUtils;
import utils.ShellCommandExecutor;
import utils.XmlParser;
import pojos.Exercise;
import pojos.Exercises;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.bind.JAXBException;
import java.io.File;

@Controller
public class JavaExercisesController {
    public static final String FILE_NAME = "tmp";
    public static final String FILE_EXTANTION = ".java";
    public static final String COMPILED_FILE_EXTANTION = ".class";
    public static final String CLASS_NAME = "public class Base";
    public static final String UPDATED_CLASS_NAME = "public class ";
    public static final String FRAGMENTS_SECTIONS_JAVA_EXERCISES_SECTION = "fragments/sections/javaExercisesSection";
    private Exercises exercises = null;

    @RequestMapping(value = "/javaExercisesSection", params = {"subChapter"}, method = RequestMethod.GET)
    public String getJavaTraining(@RequestParam(value = "name", required = false, defaultValue = "Lobster") String name, Model model, @RequestParam String subChapter) {
        model.addAttribute("name", name);
        model.addAttribute("view", FRAGMENTS_SECTIONS_JAVA_EXERCISES_SECTION);

        try {
            exercises = XmlParser.parseExercisesXml(subChapter);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        Code code = new Code();
        model.addAttribute("code", code);
        model.addAttribute("exercises", exercises.getExercises());

        return "default";
    }

    @RequestMapping(value = "/processCode", params = {"exNumber"}, method = RequestMethod.POST)
    public String getProcessCode(@RequestParam(value = "name", required = false, defaultValue = "Lobster") String name, @ModelAttribute(value = "code") Code code, Model model, @RequestParam String exNumber) throws Exception {
        File file = File.createTempFile(FILE_NAME, FILE_EXTANTION);
        file.createNewFile();

        String fileName = file.getName().replace(FILE_EXTANTION, "");
        String receivedCode = code.getCodeToProcess().replace(CLASS_NAME, UPDATED_CLASS_NAME + fileName);
        FileWriteUtils.writeToFile(receivedCode, file.getAbsolutePath());

        String result = null;
        if (file != null) {
            result = ShellCommandExecutor.runCode(file);
        }
        new File(file.getAbsolutePath().replace(FILE_EXTANTION, COMPILED_FILE_EXTANTION)).delete();
        file.delete();

        model.addAttribute("view", FRAGMENTS_SECTIONS_JAVA_EXERCISES_SECTION);
        model.addAttribute("name", name);
        model.addAttribute("exercises", exercises.getExercises());
        if (result != null) {
            model.addAttribute("result", result);
            for (Exercise exercise : exercises.getExercises()) {
                if (exercise.getNumber().equals(exNumber)) {
                    model.addAttribute("successful", result.replace("\n", "").toLowerCase().equals(exercise.getResult().replace("\n", "").toLowerCase()));
                }
            }
        }

        return "default";
    }
}
