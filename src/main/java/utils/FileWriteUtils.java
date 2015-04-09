package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileWriteUtils {
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
}