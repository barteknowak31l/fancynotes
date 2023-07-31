package com.fazdevguy.fancynotes.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {


    @EmbeddedId
    private RoleKey roleId;

    public Role(){}

    public Role(RoleKey roleId) {
        this.roleId = roleId;
    }

    public RoleKey getRoleId() {
        return roleId;
    }

    public void setRoleId(RoleKey roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                '}';
    }
}
