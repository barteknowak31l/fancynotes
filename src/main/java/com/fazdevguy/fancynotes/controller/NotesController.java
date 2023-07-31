package com.fazdevguy.fancynotes.controller;

import com.fazdevguy.fancynotes.entity.Category;
import com.fazdevguy.fancynotes.entity.Note;
import com.fazdevguy.fancynotes.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/notes")
public class NotesController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/showAll")
    public String showAllNotes(@RequestParam(value = "categoryId") Integer categoryId, Model model){

        Category category = categoryService.findCategoryById(categoryId);
        List<Note> notesList = category.getNotesList();

        model.addAttribute("category",category);
        model.addAttribute("notesList",notesList);

        return "notes-list";

    }

}
