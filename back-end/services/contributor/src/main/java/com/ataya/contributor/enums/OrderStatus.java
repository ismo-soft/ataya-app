package com.ataya.contributor.enums;

public enum OrderStatus {

    REJECTED,
    COMPLETED,
    CANCELLED,
    POSTED,
    PENDING,
    INVOICED;



    public static OrderStatus fromString(String status) {
        if (status == null) {
            return null;
        }
        try {
            return OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // or throw an exception if you prefer
        }
    }

    // isValid method to check if the status is valid ignoring case
    public static boolean isValid(String status) {
        if (status == null) {
            return false;
        }
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.name().equalsIgnoreCase(status)) {
                return true;
            }
        }
        return false;
    }

}
