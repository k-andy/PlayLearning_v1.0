package com.playlearning.dao;

import com.playlearning.model.Exercise;
import com.playlearning.model.Lesson;

import java.util.List;

/**
 * Created by andy on 5/9/15.
 */
public interface ExerciseDao {
    void addExercise(Exercise exercise);
    Exercise getExerciseById(int id);
    Exercise getExercise(int number, Lesson lesson);
    int getLastExerciseNumber(Lesson lesson);
    List<Exercise> getAllExercisesForLesson(Lesson lesson);
    void deleteExercise(Exercise exercise);
}
