package com.fazdevguy.fancynotes.userdetails;


import com.fazdevguy.fancynotes.entity.User;
import com.fazdevguy.fancynotes.service.UserService;
import com.fazdevguy.fancynotes.userdetails.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


public class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private UserService userService;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findUserByUsername(username);

        System.out.println("=====>>> Custom user details service looking for a user!");

        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }


        System.out.println("=====>>> Custom user details service found a user!");

        return new CustomUserDetails(user);
    }



}
