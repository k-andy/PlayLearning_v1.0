package com.playlearning.dao;

import com.playlearning.model.Exercise;
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
public class MethodsDaoImpl implements MethodsDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addMethod(Method method) {
        sessionFactory.getCurrentSession().saveOrUpdate(method);
    }

    @Override
    public Method getMethodById(int id) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from Method where id=?")
                .setParameter(0, id)
                .list();

        return list.isEmpty() ? null : (Method) list.get(0);
    }

    @Override
    public Method getMethodForExercise(Exercise exercise) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from Method where exercisesByExerciseId=?")
                .setParameter(0, exercise).list();

        return list.isEmpty() ? null : (Method) list.get(0);
    }

    @Override
    public void deleteMethod(Method method) {
        sessionFactory.getCurrentSession().delete(method);
    }
}
