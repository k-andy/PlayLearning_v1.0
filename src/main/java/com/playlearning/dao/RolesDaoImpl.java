package com.playlearning.dao;

import com.playlearning.model.Role;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class RolesDaoImpl implements RolesDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addUserRole(Role role) {
        sessionFactory.getCurrentSession().save(role);
    }

    @Override
    public Role getRoleById(int id) {
        return (Role) sessionFactory.getCurrentSession()
                .createQuery("from Role where id=?")
                .setParameter(0, id)
                .list().get(0);
    }

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles;

        roles = sessionFactory.getCurrentSession()
                .createQuery("from Role ")
                .list();

        if (roles.size() > 0) {
            return roles;
        } else {
            return null;
        }
    }

    @Override
    public void deleteRole(Role role) {
        sessionFactory.getCurrentSession().delete(role);
    }
}
