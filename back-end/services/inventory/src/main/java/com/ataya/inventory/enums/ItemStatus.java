package com.ataya.inventory.enums;

import lombok.Getter;

@Getter
public enum ItemStatus {
    AVAILABLE("available"),
    SOLD("sold"),
    PENDING("pending"),
    UNAVAILABLE("unavailable"),
    DELETED("deleted"),
    OTHER("other");

    private final String status;

    ItemStatus(String status) {
        this.status = status;
    }


    // check if the status exists ignoring case
    public static boolean isValidStatus(String status) {
        for (ItemStatus itemStatus : ItemStatus.values()) {
            if (itemStatus.getStatus().equalsIgnoreCase(status)) {
                return true;
            }
        }
        return false;
    }

    // get the status by name ignoring case
    public static ItemStatus getStatusByName(String status) {
        for (ItemStatus itemStatus : ItemStatus.values()) {
            if (itemStatus.getStatus().equalsIgnoreCase(status)) {
                return itemStatus;
            }
        }
        return null;
    }


}
