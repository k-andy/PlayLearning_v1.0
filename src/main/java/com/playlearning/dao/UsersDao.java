package com.playlearning.dao;

import com.playlearning.model.User;

import java.util.List;

public interface UsersDao {
    List<User> getAllUsers();
    User getUserById(int id);
    User findByUsername(String username);
    void addUser(User user);
    void deleteUser(User user);
}
