package com.fazdevguy.fancynotes.service;

import com.fazdevguy.fancynotes.dao.CategoryDAO;
import com.fazdevguy.fancynotes.dao.CategoryDAOImpl;
import com.fazdevguy.fancynotes.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryServiceImpl implements CategoryService{

    private CategoryDAO categoryDAO;

    public CategoryServiceImpl(){};

    @Autowired
    public CategoryServiceImpl(CategoryDAO dao)
    {
        this.categoryDAO = dao;
    }


    @Override
    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    @Override
    public Category findCategoryById(int id) {
        return categoryDAO.findCategoryById(id);
    }

    @Override
    public Category save(Category category) {
        return categoryDAO.save(category);
    }

    @Override
    public void deleteCategoryById(int id) {

        categoryDAO.deleteCategoryById(id);
    }

    @Override
    public Category findCategoryWithNotesById(int id) {
        return categoryDAO.findCategoryWithNotesById(id);
    }
}
