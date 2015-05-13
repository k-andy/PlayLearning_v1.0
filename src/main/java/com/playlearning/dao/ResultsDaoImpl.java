package com.playlearning.dao;

import com.playlearning.model.Exercise;
import com.playlearning.model.Result;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class ResultsDaoImpl implements ResultsDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addResult(Result result) {
        sessionFactory.getCurrentSession().saveOrUpdate(result);
    }

    @Override
    public Result getResultById(int id) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from Result where id=?")
                .setParameter(0, id)
                .list();

        return list.isEmpty() ? null : (Result) list.get(0);
    }

    @Override
    public Result getResultForExercise(Exercise exercise) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from Result where exercisesByExerciseId=?")
                .setParameter(0, exercise).list();

        return list.isEmpty() ? null : (Result) list.get(0);
    }

    @Override
    public void deleteResult(Result result) {
        sessionFactory.getCurrentSession().delete(result);
    }
}
