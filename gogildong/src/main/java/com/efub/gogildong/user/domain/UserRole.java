package com.efub.gogildong.user.domain;

public enum UserRole {
    INTERNAL, EXTERNAL, ADMIN, SUPER_ADMIN;
    public boolean isInternal() {
        return this == INTERNAL;
    }
}
