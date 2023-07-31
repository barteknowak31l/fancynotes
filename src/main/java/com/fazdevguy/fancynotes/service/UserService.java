package com.fazdevguy.fancynotes.service;

import com.fazdevguy.fancynotes.entity.User;

import java.util.List;

public interface UserService {

    User save(User user);

    User findUserById(int id);

    User findUserByUsername(String username);

    User findUserByUsernameWithCategories(String username);

    List<User> findAllUsers();

    void deleteUserById(int id);


    User findUserWithCategoriesById(int id);

}
