package com.fazdevguy.fancynotes.service;

import com.fazdevguy.fancynotes.dao.RoleDAO;
import com.fazdevguy.fancynotes.dao.RoleDAOImpl;
import com.fazdevguy.fancynotes.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{

    private RoleDAO roleDAO;

    public RoleServiceImpl(){}

    @Autowired
    public RoleServiceImpl(RoleDAO dao){
        this.roleDAO = dao;
    }

    @Override
    public Role findRoleByName(String theRoleName) {
        return roleDAO.findRoleByName(theRoleName);
    }

    @Override
    public void save(Role role) {
        roleDAO.save(role);
    }

    @Override
    public Role find(Role role) {
        return roleDAO.find(role);
    }
}
