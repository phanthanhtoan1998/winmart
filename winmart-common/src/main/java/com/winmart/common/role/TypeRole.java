package com.winmart.common.role;


public enum TypeRole {
    File(0, "Check role base on config in role.properties file"),
    AUTO(1, "Auto generate role"),
    FIX(2, "Fix role");
    private final Integer value;
    private final String description;

    TypeRole(Integer value, String description) {
        this.value = value;
        this.description = description;
    }
}
