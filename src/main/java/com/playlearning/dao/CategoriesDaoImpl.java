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
public class CategoriesDaoImpl implements CategoriesDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addCategory(Category category) {
        sessionFactory.getCurrentSession().saveOrUpdate(category);
    }

    @Override
    public Category getCategoryById(int id) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from Category where id=?")
                .setParameter(0, id)
                .list();

        return list.isEmpty() ? null : (Category) list.get(0);
    }

    @Override
    public Category getCategory(int number, String name, Course course) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from Category where number=? and name=? and coursesByCourseId=?")
                .setParameter(0, number)
                .setParameter(1, name)
                .setParameter(2, course)
                .list();

        return list.isEmpty() ? null : (Category) list.get(0);
    }

    @Override
    public List<Category> getAllCategoriesForCourse(Course Course) {
        List<Category> categories;

        categories = sessionFactory.getCurrentSession()
                .createQuery("from Category where coursesByCourseId=? order by number")
                .setParameter(0, Course).list();

        if (categories.size() > 0) {
            return categories;
        } else {
            return null;
        }
    }

    @Override
    public int getLastCategoryNumber(Course course) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("select number from Category where coursesByCourseId=? order by number desc ")
                .setParameter(0, course).list();
        return list.isEmpty() ? 0 : (Integer) list.get(0);
    }

    @Override
    public void deleteCategory(Category category) {
        sessionFactory.getCurrentSession().delete(category);
    }
}
