package com.fazdevguy.fancynotes.userdetails;

import com.fazdevguy.fancynotes.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User theUser) {this.user = theUser;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser(){return  user;}


    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<com.fazdevguy.fancynotes.entity.Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleId().getRole())).collect(Collectors.toList());
    }

}
