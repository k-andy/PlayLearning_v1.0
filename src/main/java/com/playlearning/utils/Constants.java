package com.playlearning.utils;

import java.io.File;

/**
 * Created by andy on 5/18/15.
 */
public enum Constants {
    LESSON_CONTENT_TYPE("lesson"),
    EXERCISE_CONTENT_TYPE("exercise"),
    COURSES_PATH(System.getProperty("user.dir") + File.separator + "src/main/resources/pages/fragments/articles/courses"),
    COURSES_PATH_INTERNAL("fragments/articles/courses"),
    RESULT_TYPE_OUTPUT("output"),
    RESULT_TYPE_COMPILE("compile"),
    CATEGORIES_DIRECTORY_NAME("categories"),
    LESSONS_DIRECTORY_NAME("lessons"),
    EXERCISES_DIRECTORY_NAME("exercises"),
    PAGE_EXTENSION(".html"),
    SPACE_REPLACEMENT("_"),
    HTML_FILE_NAME_REPLACE_PATTERN("[^a-zA-Z0-9]+"),
    CATEGORIES_FOLDER_NAME("categories"),
    LESSONS_FOLDER_NAME("lessons");


    private String value;

    private Constants(String value) {
        this.value = value;
    }

    public String getConstant() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
