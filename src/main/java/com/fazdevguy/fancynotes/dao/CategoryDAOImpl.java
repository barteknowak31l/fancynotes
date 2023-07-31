package com.fazdevguy.fancynotes.dao;

import com.fazdevguy.fancynotes.entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.lang.model.element.TypeElement;
import java.util.List;

@Repository
public class CategoryDAOImpl implements CategoryDAO{

    private EntityManager em;

    public CategoryDAOImpl(){};

    @Autowired
    public CategoryDAOImpl(EntityManager entityManager)
    {
        this.em = entityManager;
    }

    @Override
    public List<Category> findAll() {

        TypedQuery<Category> query = em.createQuery("from Category c", Category.class);
        List<Category> categoryList = query.getResultList();

        return categoryList;
    }

    @Override
    @Transactional
    public Category findCategoryById(int id) {

        Category category =em.find(Category.class,id);
        Hibernate.initialize(category.getNotesList());
        return category;
    }

    @Override
    @Transactional
    public Category save(Category category) {
        return em.merge(category);
    }

    @Override
    @Transactional
    public void deleteCategoryById(int id) {

        Category category = em.find(Category.class,id);

        em.remove(category);

    }

    @Override
    public Category findCategoryWithNotesById(int id) {

        TypedQuery<Category> query = em.createQuery("select c from Category c " +
                "left join fetch c.notesList " +
                "where c.id =:data", Category.class);
        query.setParameter("data",id);

        Category category = query.getSingleResult();

        return category;
    }
}
