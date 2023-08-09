package com.fazdevguy.fancynotes.controller;

import com.fazdevguy.fancynotes.entity.Category;
import com.fazdevguy.fancynotes.entity.Role;
import com.fazdevguy.fancynotes.entity.User;
import com.fazdevguy.fancynotes.service.RoleService;
import com.fazdevguy.fancynotes.service.UserService;
import com.fazdevguy.fancynotes.userdetails.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class DemoController {


    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;


    @GetMapping("/hello")
    public String hello(Model model, Principal principal) {

        // find user by username
        String username = principal.getName();
        User user = userService.findUserByUsername(username);

        List<Role> userRoles = roleService.findAllRolesByUsername(username);

        List<String> roles = new ArrayList<>();
        for(Role role : userRoles){
            roles.add(role.getRoleId().getRole());
        }


        if(roles.contains("ROLE_ADMIN")) {
            model.addAttribute("current_date",new Date());
            model.addAttribute("usr",user);
            return "helloworld";
        }
        else{
            List<Category> categoryList = user.getCategoryList();
            model.addAttribute("categoryList",categoryList);
            return "redirect:/categories/showAll";
        }

    }

    // mapping for access denied
    @GetMapping("/access-denied")
    public String showAccessDenied(){
        return "access-denied";
    }




}
