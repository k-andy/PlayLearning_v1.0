package com.playlearning.utils;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileWriteUtils {
    public static final String LESSON_HEADER = "lessonHeader";
    public static final String LESSON_TEXT = "lessonText";
    public static final String PARAGRAPH = "paragraph";
    public static final String ORDERED_LIST = "orderedList";
    public static final String UNORDERED_LIST = "unorderedList";
    public static final String CODE_SNIPPET = "codeSnippet";
    public static final String IMAGE = "image";
    private static Logger logger = Logger.getLogger(FileWriteUtils.class.toString());

    public static void writeToFile(String content, String filePath) {
        Writer writer = null;
        try {
            writer = new FileWriter(filePath);
            writer.write(content);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error writing the file : " + filePath);
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Error closing the file : " + filePath);
                    e.printStackTrace();
                }
            }
        }
    }

    public static String readFile(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        File text = new File(filePath);

        Scanner scanner = null;
        try {
            scanner = new Scanner(text);
        } catch (FileNotFoundException e) {
            logger.log(Level.WARNING, "Error reading the file : " + filePath);
        }

        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public static void deleteDirectory(File file) throws IOException {
        Path directoriesToDelete = Paths.get(file.getParentFile().getParentFile().getAbsolutePath());
        Files.walkFileTree(directoriesToDelete, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }

        });
    }

    public static void createHtmlFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Couldn't create " + file.getName());
            }
        }
    }

    public static String createHtmlContent(Constants contentType, String contentName) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<!DOCTYPE html>\n");
        stringBuilder.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:th=\"http://www.thymeleaf.org\">\n");
        stringBuilder.append("<head>\n");
        stringBuilder.append("<title>" + contentName + "</title>\n");
        stringBuilder.append("</head>\n");
        stringBuilder.append("<body>\n");
        if (Constants.LESSON_CONTENT_TYPE.equals(contentType)) {
            stringBuilder.append("<article class=\"" + contentType + "Wrapper\"" + " th:fragment=\"lesson\">\n");
            stringBuilder.append("<div class=\"" + LESSON_HEADER + "Wrapper\"></div>\n");
            stringBuilder.append("<div class=\"" + LESSON_TEXT + "Wrapper\">\n");
            stringBuilder.append("<div class=\"" + PARAGRAPH + "Wrapper\"></div>");
            stringBuilder.append("</div>\n");
            stringBuilder.append("<div class=\"" + ORDERED_LIST + "Wrapper\"></div>\n");
            stringBuilder.append("<div class=\"" + UNORDERED_LIST + "Wrapper\"></div>\n");
            stringBuilder.append("<div class=\"" + CODE_SNIPPET + "Wrapper\"></div>\n");
            stringBuilder.append("<div class=\"" + IMAGE + "Wrapper\"></div>\n");
        } else if (Constants.EXERCISE_CONTENT_TYPE.equals(contentType)) {
            stringBuilder.append("<article class=\"\"" + "th:fragment=\"exercise\">\n");
            stringBuilder.append("<div class=\"" + contentType + "Wrapper\">\n");
            stringBuilder.append("<div class=\"" + "exerciseHeader" + "Wrapper\"></div>\n");
            stringBuilder.append("<div class=\"" + "exerciseText" + "Wrapper\">\n");
            stringBuilder.append("<div class=\"" + PARAGRAPH + "Wrapper\"></div>");
            stringBuilder.append("</div>\n");
            stringBuilder.append("<div class=\"" + ORDERED_LIST + "Wrapper\"></div>\n");
            stringBuilder.append("<div class=\"" + UNORDERED_LIST + "Wrapper\"></div>\n");
            stringBuilder.append("<div class=\"" + CODE_SNIPPET + "Wrapper\"></div>\n");
            stringBuilder.append("<div class=\"" + IMAGE + "Wrapper\"></div>\n");
        }
        stringBuilder.append("</article>\n");
        stringBuilder.append("</body>\n");
        stringBuilder.append("</html>\n");

        return stringBuilder.toString();
    }
}