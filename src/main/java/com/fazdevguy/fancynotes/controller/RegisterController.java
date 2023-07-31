package com.fazdevguy.fancynotes.controller;

import com.fazdevguy.fancynotes.entity.Role;
import com.fazdevguy.fancynotes.entity.RoleKey;
import com.fazdevguy.fancynotes.entity.User;
import com.fazdevguy.fancynotes.service.RoleService;
import com.fazdevguy.fancynotes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/registrationForm")
    public String registrationForm(Model model){

        model.addAttribute("user", new User());

        return "signup-form";

    }


    @PostMapping("/process_register")
    public String processRegister(@ModelAttribute("user") User user, Model theModel, RedirectAttributes redirectAttributes)
    {

        // username and password validation
        // details must be at least 3 signs long
        if( user.getUsername().length() < 3 || user.getPassword().length() < 3){
            theModel.addAttribute("user",new User());
            theModel.addAttribute("registerFailDataTooShort",true);
            return "signup-form";
        }

        // check if password contains white spaces
        if(user.getPassword().contains(" "))
        {
            theModel.addAttribute("user",new User());
            theModel.addAttribute("registerFailPasswordWhiteSpaces",true);
            return "signup-form";
        }

        // Encode the password using Bcrypt
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword("{bcrypt}"+encodedPassword);

        // check if user exists here
        if(userService.findUserByUsername(user.getUsername()) != null){

            theModel.addAttribute("user",new User());
            theModel.addAttribute("registerFailUserExists",true);
            return "signup-form";
        }


        // check if first name and last name are provided
        if(user.getFirstName().isBlank() || user.getLastName().isBlank())
        {
            System.out.println("=====>>> MAMY NULLE!");

            theModel.addAttribute("user", new User());
            theModel.addAttribute("registerFailNoFirstOrLastName",true);
            return "signup-form";
        }

        // add new user
        User savedUser = userService.save(user);


        // add role to this user
        Role role = new Role();
        RoleKey roleKey = new RoleKey(user.getUsername(),"ROLE_EMPLOYEE");
        role.setRoleId(roleKey);
        roleService.save(role);

        // IMPORTANT!!! while REDIRECTING we have to use RedirectAttributes interface (it's injected) to pass
        // parameters between sites when redirecting
        // add model parameters to display info about succesful registration
        redirectAttributes.addFlashAttribute("registerSuccess",true);


        return "redirect:/login/loginPage";



    }

}
