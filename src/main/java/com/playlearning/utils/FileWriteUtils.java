package com.playlearning.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileWriteUtils {
    private static Logger logger = Logger.getLogger(FileWriteUtils.class.toString());

    public static void writeToFile(String content, String filePath) {
        Writer writer = null;
        try {
            writer = new FileWriter(filePath);
            writer.write(content);
        } catch (IOException e) {
            System.err.println("Error writing the file : ");
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("Error closing the file : ");
                    e.printStackTrace();
                }
            }
        }
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

    public static void writeHtmlFile(String content, String filePath) {
        Writer writer = null;
        try {
            writer = new FileWriter(filePath);
            writer.write(content);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Couldn't write file " + filePath);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Couldn't close file " + filePath);
                }
            }

        }
    }

    public static String createHtmlContent(String contentType, String contentName) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<!DOCTYPE html>\n");
        stringBuilder.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:th=\"http://www.thymeleaf.org\">\n");
        stringBuilder.append("<head>\n");
        stringBuilder.append("<title>" + contentName + "</title>\n");
        stringBuilder.append("</head>\n");
        stringBuilder.append("<body>\n");
        stringBuilder.append("<article th:fragment=\"content\">\n");
        stringBuilder.append("<div class=\"" + contentType + "Wrapper\">\n");
        stringBuilder.append("<div class=\"" + contentType + "HeaderWrapper\">\n");
        stringBuilder.append("<h2>\n");
        stringBuilder.append(contentName);
        stringBuilder.append("</h2>\n");
        stringBuilder.append("</div>\n");
        stringBuilder.append("<div class=\"" + contentType + "ContentWrapper\">\n");
        stringBuilder.append("content goes here\n");
        stringBuilder.append("</div>\n");
        stringBuilder.append("</div>\n");
        stringBuilder.append("</article>\n");
        stringBuilder.append("</body>\n");
        stringBuilder.append("</html>\n");

        return stringBuilder.toString();
    }
}