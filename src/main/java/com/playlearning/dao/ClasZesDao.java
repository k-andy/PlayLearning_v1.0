package com.playlearning.dao;

import com.playlearning.model.*;

import java.util.List;

/**
 * Created by andy on 5/11/15.
 */
public interface ClasZesDao {
    void addClasZ(ClasZ clasZ);
    ClasZ getClasZById(int id);
    ClasZ getClasZForExercise(Exercise exercise);
    void deleteClasZ(ClasZ clasZ);
}
