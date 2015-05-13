package com.playlearning.dao;

import com.playlearning.model.ClasZ;
import com.playlearning.model.Exercise;
import com.playlearning.model.Method;

import java.util.List;

/**
 * Created by andy on 5/11/15.
 */
public interface MethodsDao {
    void addClasZ(Method method);
    Method getMethodById(int id);
    Method getMethodForExercise(Exercise exercise);
    void deleteMethod(Method method);
}
