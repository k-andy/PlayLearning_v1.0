package com.playlearning.dao;

import com.playlearning.model.Category;
import com.playlearning.model.Course;
import com.playlearning.model.Lesson;

import java.util.List;

/**
 * Created by andy on 5/9/15.
 */
public interface LessonsDao {
    void addLesson(Lesson lesson);
    Lesson getLessonById(int id);
    Lesson getLesson(int number, Category category);
    List<Lesson> getAllLessonsForCategory(Category category);
    int getLastLessonNumber(Category category);
    void deleteLesson(Lesson lesson);
}
