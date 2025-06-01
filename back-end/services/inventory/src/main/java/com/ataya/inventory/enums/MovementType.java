package com.ataya.inventory.enums;

public enum MovementType {
    NEW,
    INCOMING,
    SUSPEND,
    UNSUSPEND,
    OUTGOING,
    ADJUSTMENT,
    TRANSFER,
    RETURN;

    public static MovementType fromString(String type) {
        for (MovementType movementType : MovementType.values()) {
            if (movementType.name().equalsIgnoreCase(type)) {
                return movementType;
            }
        }
        throw new IllegalArgumentException("Unknown movement type: " + type);
    }

    // is valid movement type
    public static boolean isValid(String type) {
        for (MovementType movementType : MovementType.values()) {
            if (movementType.name().equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

}
