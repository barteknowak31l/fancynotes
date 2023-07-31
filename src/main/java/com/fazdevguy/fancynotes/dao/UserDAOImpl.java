package com.fazdevguy.fancynotes.dao;

import com.fazdevguy.fancynotes.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private EntityManager em;

    @Autowired
    public UserDAOImpl(EntityManager entityManager){
        this.em = entityManager;
    }

    @Override
    @Transactional
    public User save(User user) {
       return em.merge(user);
    }

    @Override
    @Transactional
    public User findUserById(int id) {

        User user =em.find(User.class, id);
        Hibernate.initialize(user.getCategoryList());
        return user;
    }

    @Override
    public User findUserByUsername(String username) {

        TypedQuery<User> query = em.createQuery("select u from User u " +
                "where u.username =:data", User.class);
        query.setParameter("data", username);

        User  theUser= null;

        try{
            theUser = query.getSingleResult();
        }
        catch (Exception e)
        {
          theUser = null;
        }

        return theUser;
    }

    @Override
    public User findUserByUsernameWithCategories(String username) {

        TypedQuery<User> query = em.createQuery("select u from User u " +
                "left join fetch u.categoryList " +
                "where u.username =:data", User.class);
        query.setParameter("data",username);

        User user = query.getSingleResult();

        return user;
    }

    @Override
    public List<User> findAllUsers() {

        TypedQuery<User> query = em.createQuery("from User u",User.class);

        List<User> users = query.getResultList();

        return users;
    }

    @Override
    public void deleteUserById(int id) {
        User user = findUserById(id);

        if(user != null){

            em.remove(user);
        }

    }

    @Override
    public User findUserWithCategoriesById(int id) {



        TypedQuery<User> query = em.createQuery("select u from User u " +
                " left join fetch u.categoryList " +
                "where u.id =:data", User.class);
        query.setParameter("data",id);

        User user = query.getSingleResult();

        return user;


    }

}
