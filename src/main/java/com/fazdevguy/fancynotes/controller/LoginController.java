package com.fazdevguy.fancynotes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {


    // Mapping for login page - model can have attribute bool registerSuccess set to
    // true when user has just registered via register page and has been redirected
    // to this page
    @GetMapping("/loginPage")
    public String loginPage(Model model){
        Boolean s = (Boolean)model.getAttribute("registerSuccess");

        return "fancy-login";
    }




}
