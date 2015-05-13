package com.playlearning.dao;

import com.playlearning.model.Exercise;
import com.playlearning.model.Result;

/**
 * Created by andy on 5/9/15.
 */
public interface ResultsDao {
    void addResult(Result result);
    Result getResultById(int id);
    Result getResultForExercise(Exercise exercise);
    void deleteResult(Result result);
}
