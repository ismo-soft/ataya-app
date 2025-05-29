package com.ataya.contributor.enums;

public enum ShoppingCartStatus {
    ACTIVE,
    EMPTY,
    CHECKED_OUT;

    public static ShoppingCartStatus fromString(String status) {
        for (ShoppingCartStatus cartStatus : ShoppingCartStatus.values()) {
            if (cartStatus.name().equalsIgnoreCase(status)) {
                return cartStatus;
            }
        }
        throw new IllegalArgumentException("Unknown shopping cart status: " + status);
    }

    public static boolean isValidStatus(String status) {
        for (ShoppingCartStatus cartStatus : ShoppingCartStatus.values()) {
            if (cartStatus.name().equalsIgnoreCase(status)) {
                return true;
            }
        }
        return false;
    }


}
