package com.fazdevguy.fancynotes.dao;

import com.fazdevguy.fancynotes.entity.Role;

public interface RoleDAO {
    Role findRoleByName(String theRoleName);

    void save(Role role);
    Role find(Role role);

}
