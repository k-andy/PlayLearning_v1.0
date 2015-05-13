package com.playlearning.dao;

import com.playlearning.model.Exercise;
import com.playlearning.model.Field;

import java.util.List;

/**
 * Created by andy on 5/11/15.
 */
public interface FieldsDao {
    void addField(Field field);
    Field getFieldById(int id);
    Field getFieldForExercise(Exercise exercise);
    void deleteField(Field field);
}
