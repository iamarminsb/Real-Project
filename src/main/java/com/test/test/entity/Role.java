package com.test.test.entity;

public enum Role {
    DOCTOR,
    NURSE,
    PATIENT;
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
