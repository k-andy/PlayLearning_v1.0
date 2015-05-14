package com.playlearning.dao;

import com.playlearning.model.Category;
import com.playlearning.model.Lesson;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class LessonsDaoImpl implements LessonsDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addLesson(Lesson lesson) {
        sessionFactory.getCurrentSession().saveOrUpdate(lesson);
    }

    @Override
    public Lesson getLessonById(int id) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from Lesson where id=?")
                .setParameter(0, id)
                .list();

        return list.isEmpty() ? null : (Lesson) list.get(0);
    }

    @Override
    public Lesson getLesson(int number, Category category) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from Lesson where number=? and categoriesByCategoryId=?")
                .setParameter(0, number)
                .setParameter(1, category)
                .list();

        return list.isEmpty() ? null : (Lesson) list.get(0);
    }

    @Override
    public List<Lesson> getAllLessonsForCategory(Category category) {
        List<Lesson> lessons;

        lessons = sessionFactory.getCurrentSession()
                .createQuery("from Lesson where categoriesByCategoryId=? order by number")
                .setParameter(0, category).list();

        if (lessons.size() > 0) {
            return lessons;
        } else {
            return null;
        }
    }

    @Override
    public int getLastLessonNumber(Category category) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("select number from Lesson where categoriesByCategoryId=? order by number desc ")
                .setParameter(0, category).list();
        return list.isEmpty() ? 0 : (Integer) list.get(0);
    }

    @Override
    public void deleteLesson(Lesson lesson) {
        sessionFactory.getCurrentSession().delete(lesson);
    }
}
