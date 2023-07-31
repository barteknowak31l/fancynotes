package com.fazdevguy.fancynotes.controller;

import com.fazdevguy.fancynotes.entity.Category;
import com.fazdevguy.fancynotes.entity.User;
import com.fazdevguy.fancynotes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private UserService userService;


    @GetMapping("/showAll")
    public String showAllCategories(Model model, Principal principal){

        String username = principal.getName();
        User user = userService.findUserByUsernameWithCategories(username);
        List<Category> categoryList = user.getCategoryList();

        model.addAttribute("categoryList",categoryList);

        return "category-list";

    }

    // get mapping for "/addForm"
    @GetMapping("/addForm")
    public String addCategoryForm(Model model){

        model.addAttribute("category", new Category());

        return "add-category-form";

    }

    // post mapping /addCategory
    @PostMapping("/addCategory")
    public String addCategory(@ModelAttribute("category") Category category, Principal principal, Model model){


        if(category.getName().isBlank())
        {
            model.addAttribute("category", new Category());
            model.addAttribute("emptyNameError",true);
            return "add-category-form";
        }

        // get user db handle
        User user = userService.findUserByUsernameWithCategories(principal.getName());

        // add category to user and save
        try{
            user.addCategory(category);
            userService.save(user);
        }
        catch (Exception e){

           model.addAttribute("category", new Category());
           model.addAttribute("saveError",true);
           return "add-category-form";
        }

        // redirect to category list
        return "redirect:/categories/showAll";

    }

}
