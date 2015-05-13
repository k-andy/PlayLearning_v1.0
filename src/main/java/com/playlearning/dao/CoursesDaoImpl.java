package com.playlearning.dao;

import com.playlearning.model.Category;
import com.playlearning.model.Course;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class CoursesDaoImpl implements CoursesDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addCourse(Course course) {
        sessionFactory.getCurrentSession().saveOrUpdate(course);
    }

    @Override
    public Course getCourseById(int id) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from Course where id=?")
                .setParameter(0, id)
                .list();

        return list.isEmpty() ? null : (Course) list.get(0);
    }

    @Override
    public Course getCourseByName(String name) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from Course where name=?")
                .setParameter(0, name)
                .list();

        return list.isEmpty() ? null : (Course) list.get(0);
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses;

        courses = sessionFactory.getCurrentSession()
                .createQuery("from Course ")
                .list();

        if (courses.size() > 0) {
            return courses;
        } else {
            return null;
        }
    }

    @Override
    public void deleteCourse(Course course) {
        sessionFactory.getCurrentSession().delete(course);
    }
}
