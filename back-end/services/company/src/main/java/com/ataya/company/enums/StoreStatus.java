package com.ataya.company.enums;

public enum StoreStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED,
    DELETED;

    // check if the status exist ignore case
    public static boolean isStatusExist(String status) {
        for (StoreStatus s : StoreStatus.values()) {
            if (s.name().equalsIgnoreCase(status)) {
                return true;
            }
        }
        return false;
    }

    // to get the status by name
    public static StoreStatus getStatus(String status) {
        for (StoreStatus s : StoreStatus.values()) {
            if (s.name().equalsIgnoreCase(status)) {
                return s;
            }
        }
        return null;
    }


}
