package com.ataya.contributor.enums;

public enum ShoppingCartMovementType {
    NEW,
    ADD,
    REMOVE,
    INCREASE_QUANTITY,
    DECREASE_QUANTITY,
    POST;

    public static ShoppingCartMovementType fromString(String type) {
        for (ShoppingCartMovementType movementType : ShoppingCartMovementType.values()) {
            if (movementType.name().equalsIgnoreCase(type)) {
                return movementType;
            }
        }
        throw new IllegalArgumentException("Unknown ShoppingCartMovementType: " + type);
    }

    public static boolean isValidType(String type) {
        for (ShoppingCartMovementType movementType : ShoppingCartMovementType.values()) {
            if (movementType.name().equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }


}
