package com.fazdevguy.fancynotes.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class RoleKey {

    private String username;
    private String role;

    public RoleKey(){}

    public RoleKey(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleKey roleKey = (RoleKey) o;
        return Objects.equals(username, roleKey.username) && Objects.equals(role, roleKey.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, role);
    }

    @Override
    public String toString() {
        return "RoleKey{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

