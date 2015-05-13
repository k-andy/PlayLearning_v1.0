package com.playlearning.dao;

import com.playlearning.model.Exercise;
import com.playlearning.model.Field;
import com.playlearning.model.Method;
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
public class FieldsDaoImpl implements FieldsDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addField(Field field) {
        sessionFactory.getCurrentSession().saveOrUpdate(field);
    }

    @Override
    public Field getFieldById(int id) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from Field where id=?")
                .setParameter(0, id)
                .list();

        return list.isEmpty() ? null : (Field) list.get(0);
    }

    @Override
    public Field getFieldForExercise(Exercise exercise) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from Field where exercisesByExerciseId=?")
                .setParameter(0, exercise).list();

        return list.isEmpty() ? null : (Field) list.get(0);
    }

    @Override
    public void deleteField(Field field) {
        sessionFactory.getCurrentSession().delete(field);
    }
}
