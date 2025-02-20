package com.ataya.company.enums;

public enum Role {
    ROLE_ADMIN,
    ROLE_SUPER_ADMIN,
    ROLE_CEO,
    ROLE_MANAGER,
    ROLE_WORKER;

    // check if the role exist ignore case
    public static boolean isRoleExist(String role) {
        for (Role r : Role.values()) {
            if (r.name().equalsIgnoreCase(role)) {
                return true;
            }
        }
        return false;
    }

    // to get the role by name
    public static Role getRole(String role) {
        for (Role r : Role.values()) {
            if (r.name().equalsIgnoreCase(role)) {
                return r;
            }
        }
        return null;
    }

    // check if the role is valid
    public static boolean isValidRole(String role) {
        return isRoleExist(role);
    }

}
