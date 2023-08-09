package com.fazdevguy.fancynotes.service;


import com.fazdevguy.fancynotes.entity.Role;

import java.util.List;

public interface RoleService {

    Role findRoleByName(String theRoleName);

    List<Role> findAllRolesByUsername(String username);


    void save(Role role);
    Role find(Role role);

}
