package com.fazdevguy.fancynotes.dao;

import com.fazdevguy.fancynotes.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RoleDAOImpl implements RoleDAO{

    private EntityManager em;

    public RoleDAOImpl(){}

    @Autowired
    public RoleDAOImpl(EntityManager entityManager) {
        this.em = entityManager;
    }



    @Override
    public Role findRoleByName(String theRoleName) {

        TypedQuery<Role> query = em.createQuery("select r from Role r "+
                "where r.roleId.role=:data",Role.class);
        query.setParameter("data", theRoleName);
        Role role = null;

        try{
            role = query.getSingleResult();
        }
        catch (Exception e){
            role = null;
        }
        return role;
    }

    @Override
    @Transactional
    public void save(Role role) {
        em.merge(role);
    }

    @Override
    public Role find(Role role) {
        return em.find(Role.class,role);
    }
}
