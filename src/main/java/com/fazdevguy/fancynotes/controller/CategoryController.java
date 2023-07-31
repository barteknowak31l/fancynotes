package com.fazdevguy.fancynotes.controller;

import com.fazdevguy.fancynotes.entity.Category;
import com.fazdevguy.fancynotes.entity.User;
import com.fazdevguy.fancynotes.service.CategoryService;
import com.fazdevguy.fancynotes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.StringTokenizer;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;


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
    public String addCategory(@ModelAttribute("category") Category category, Principal principal, Model model,
                              @RequestParam(value = "updateParam", required = false) Boolean isUpdate,
                              @RequestParam(value = "oldCategoryId", required = false) Integer oldCategoryId){


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

            if(isUpdate != null && isUpdate){

                System.out.println("===>>> UPDATING CATEGORY " + category.getName());
                Category oldCategory = categoryService.findCategoryById(oldCategoryId);
                oldCategory.setName(category.getName());
                oldCategory.setNote(category.getNote());
                categoryService.save(oldCategory);
            }

            else{
                System.out.println("===>>> ADDING CATEGORY " + category.getName());


                user.addCategory(category);
                userService.save(user);
            }

        }
        catch (Exception e){

            e.printStackTrace();
           model.addAttribute("category", new Category());
           model.addAttribute("saveError",true);
           return "add-category-form";
        }

        // redirect to category list
        return "redirect:/categories/showAll";

    }

    // update mapping
    @GetMapping("/update")
    public String updateCategory(@RequestParam("categoryId") int categoryId, Model model){

        Category category = categoryService.findCategoryById(categoryId);

        model.addAttribute("category",category);
        model.addAttribute("oldCategoryId",category.getId());

        model.addAttribute("update",true);

        return "add-category-form";

    }


    // delete mapping
    @GetMapping("/delete")
    public String deleteCategory(@RequestParam(value = "categoryId") Integer categoryId){

        categoryService.deleteCategoryById(categoryId);

        return "redirect:/categories/showAll";
    }

}
