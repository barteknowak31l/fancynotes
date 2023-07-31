package com.fazdevguy.fancynotes.service;

import com.fazdevguy.fancynotes.dao.UserDAO;
import com.fazdevguy.fancynotes.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private UserDAO userDAO;

    public UserServiceImpl(){}

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User save(User user) {

        return  userDAO.save(user);
    }

    @Override
    public User findUserById(int id) {
        return userDAO.findUserById(id);
    }

    @Override
    public User findUserByUsername(String username) {
        return userDAO.findUserByUsername(username);
    }

    @Override
    public List<User> findAllUsers() {
        return userDAO.findAllUsers();
    }

    @Override
    @Transactional
    public void deleteUserById(int id) {
        userDAO.deleteUserById(id);
    }

    @Override
    public User findUserWithCategoriesById(int id) {
        return userDAO.findUserWithCategoriesById(id);
    }


}
