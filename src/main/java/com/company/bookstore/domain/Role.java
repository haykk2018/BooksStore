package com.company.bookstore.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    GUEST, USER, ADMIN, SUPER_ADMIN;

    @Override
    public String getAuthority() {
        return null;
    }
}