package com.example.order.enums;

public enum OrderStatus {
    PENDING,
    COMPLETED,
    CANCELLED,
    POSTED;



    public static OrderStatus fromString(String status) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.name().equalsIgnoreCase(status)) {
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("Unknown order status: " + status);
    }
    public String toString() {
        return name().toLowerCase();
    }

    public static OrderStatus fromStringOrNull(String status) {
        if (status == null || status.isEmpty()) {
            return null;
        }
        try {
            return fromString(status);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    public static boolean isValid(String status) {
        if (status == null || status.isEmpty()) {
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
