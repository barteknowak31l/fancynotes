package com.fazdevguy.fancynotes.userdetails;

import com.fazdevguy.fancynotes.entity.User;
import com.fazdevguy.fancynotes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private UserService userService;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findUserByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }


        return new CustomUserDetails(user);
    }
}
