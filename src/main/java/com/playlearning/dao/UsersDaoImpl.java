package com.playlearning.dao;

import com.playlearning.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class UsersDaoImpl implements UsersDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<User> getAllUsers() {
        List<User> users;

        users = sessionFactory.getCurrentSession()
                .createQuery("from User")
                .list();

        if (users.size() > 0) {
            return users;
        } else {
            return null;
        }
    }

    @Override
    public void addUser(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    public User getUserById(int id) {
        return (User) sessionFactory.getCurrentSession()
                .createQuery("from User where id=?")
                .setParameter(0, id)
                .list().get(0);
    }

    @Override
    public User findByUsername(String username) {
        return (User) sessionFactory.getCurrentSession()
                .createQuery("from User where name=?")
                .setParameter(0, username)
                .list().get(0);
    }

    @Override
    public void deleteUser(User user) {
        sessionFactory.getCurrentSession().delete(user);
    }
}