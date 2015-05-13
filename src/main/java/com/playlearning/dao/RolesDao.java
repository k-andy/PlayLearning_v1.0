package com.playlearning.dao;

import com.playlearning.model.Role;

import java.util.List;

public interface RolesDao {
    void addUserRole(Role role);
    Role getRoleById(int id);
    List<Role> getAllRoles();
    void deleteRole(Role role);
}
