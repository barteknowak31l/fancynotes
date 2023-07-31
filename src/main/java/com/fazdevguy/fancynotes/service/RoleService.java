package com.fazdevguy.fancynotes.service;


import com.fazdevguy.fancynotes.entity.Role;

public interface RoleService {

    Role findRoleByName(String theRoleName);

    void save(Role role);
    Role find(Role role);

}
