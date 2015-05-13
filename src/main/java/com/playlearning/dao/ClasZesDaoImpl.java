package com.playlearning.dao;

import com.playlearning.model.ClasZ;
import com.playlearning.model.Exercise;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by andy on 5/11/15.
 */
@Repository
@Transactional
public class ClasZesDaoImpl implements ClasZesDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addClasZ(ClasZ clasZ) {
        sessionFactory.getCurrentSession().saveOrUpdate(clasZ);
    }

    @Override
    public ClasZ getClasZById(int id) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from ClasZ where id=?")
                .setParameter(0, id)
                .list();

        return list.isEmpty() ? null : (ClasZ) list.get(0);
    }

    @Override
    public ClasZ getClasZForExercise(Exercise exercise) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from ClasZ where exercisesByExerciseId=?")
                .setParameter(0, exercise).list();

        return list.isEmpty() ? null : (ClasZ) list.get(0);
    }

    @Override
    public void deleteClasZ(ClasZ clasZ) {
        sessionFactory.getCurrentSession().delete(clasZ);
    }
}
