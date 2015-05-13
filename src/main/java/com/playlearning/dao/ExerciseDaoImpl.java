package com.playlearning.dao;

import com.playlearning.model.Exercise;
import com.playlearning.model.Lesson;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class ExerciseDaoImpl implements ExerciseDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addExercise(Exercise exercise) {
        sessionFactory.getCurrentSession().saveOrUpdate(exercise);
    }

    @Override
    public Exercise getExerciseById(int id) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from Exercise where id=?")
                .setParameter(0, id)
                .list();

        return list.isEmpty() ? null : (Exercise) list.get(0);
    }

    @Override
    public Exercise getExercise(int number, Lesson lesson) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from Exercise where number=? and lessonsByLessonId=?")
                .setParameter(0, number)
                .setParameter(1, lesson)
                .list();

        return list.isEmpty() ? null : (Exercise) list.get(0);
    }

    @Override
    public int getLastExerciseNumber(Lesson lesson) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("select number from Exercise where lessonsByLessonId=? order by number desc ")
                .setParameter(0, lesson).list();
        return list.isEmpty() ? 0 : (Integer) list.get(0);
    }

    @Override
    public List<Exercise> getAllExercisesForLesson(Lesson lesson) {
        List<Exercise> exercises;

        exercises = sessionFactory.getCurrentSession()
                .createQuery("from Exercise where lessonsByLessonId=?")
                .setParameter(0, lesson).list();

        if (exercises.size() > 0) {
            return exercises;
        } else {
            return null;
        }
    }

    @Override
    public void deleteExercise(Exercise exercise) {
        sessionFactory.getCurrentSession().delete(exercise);
    }
}
