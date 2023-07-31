package com.fazdevguy.fancynotes.dao;

import com.fazdevguy.fancynotes.entity.User;

import java.util.List;

public interface UserDAO {

    User save(User user);

    User findUserById(int id);

    User findUserByUsername(String username);

    List<User> findAllUsers();

    void deleteUserById(int id);

    User findUserWithCategoriesById(int id);

}
