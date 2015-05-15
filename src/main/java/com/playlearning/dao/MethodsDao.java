package com.playlearning.dao;

import com.playlearning.model.Exercise;
import com.playlearning.model.Method;

/**
 * Created by andy on 5/11/15.
 */
public interface MethodsDao {
    void addMethod(Method method);
    Method getMethodById(int id);
    Method getMethodForExercise(Exercise exercise);
    void deleteMethod(Method method);
}
