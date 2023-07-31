package com.fazdevguy.fancynotes.controller;

import com.fazdevguy.fancynotes.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;

@Controller
public class DemoController {


    @GetMapping("/hello")
    public String hello(Model model, Principal principal) {


        model.addAttribute("current_date",new Date());
        model.addAttribute("usr",principal);



        return "helloworld";
    }


}
