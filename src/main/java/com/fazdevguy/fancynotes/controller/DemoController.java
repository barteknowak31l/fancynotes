package com.fazdevguy.fancynotes.controller;

import com.fazdevguy.fancynotes.entity.User;
import com.fazdevguy.fancynotes.service.UserService;
import com.fazdevguy.fancynotes.userdetails.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;

@Controller
public class DemoController {


    @Autowired
    private UserService userService;


    @GetMapping("/hello")
    public String hello(Model model, Principal principal) {

        // find user by username
        String username = principal.getName();
        User user = userService.findUserByUsername(username);


        model.addAttribute("current_date",new Date());
        model.addAttribute("usr",user);

        return "helloworld";
    }

    // mapping for access denied
    @GetMapping("/access-denied")
    public String showAccessDenied(){
        return "access-denied";
    }




}
