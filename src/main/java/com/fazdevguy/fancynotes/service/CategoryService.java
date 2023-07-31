package com.fazdevguy.fancynotes.service;

import com.fazdevguy.fancynotes.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    Category findCategoryById(int id);

    Category save(Category category);

    void deleteCategoryById(int id);

    Category findCategoryWithNotesById(int id);

}
