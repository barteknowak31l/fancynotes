package com.fazdevguy.fancynotes.dao;

import com.fazdevguy.fancynotes.entity.Role;

import java.util.List;

public interface RoleDAO {
    Role findRoleByName(String theRoleName);

    List<Role> findAllRolesByUsername(String username);

    void save(Role role);
    Role find(Role role);

}
