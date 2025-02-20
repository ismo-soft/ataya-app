package com.ataya.address.enums;

public enum AddressTag {
    COMPANY,
    GROCERY,
    MARKET,
    RESTAURANT,
    BAKERY,
    CAFE,
    CLOTHING;

    public static AddressTag fromString(String tag) {
        for (AddressTag addressTag : AddressTag.values()) {
            if (addressTag.name().equalsIgnoreCase(tag)) {
                return addressTag;
            }
        }
        return null;
    }

    public static boolean isValid(String tag) {
        for (AddressTag addressTag : AddressTag.values()) {
            if (addressTag.name().equalsIgnoreCase(tag)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValid(AddressTag tag) {
        for (AddressTag addressTag : AddressTag.values()) {
            if (addressTag.equals(tag)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValid(String[] tags) {
        for (String tag : tags) {
            if (!isValid(tag)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValid(AddressTag[] tags) {
        for (AddressTag tag : tags) {
            if (!isValid(tag)) {
                return false;
            }
        }
        return true;
    }

    public static AddressTag[] fromString(String[] tags) {
        AddressTag[] addressTags = new AddressTag[tags.length];
        for (int i = 0; i < tags.length; i++) {
            addressTags[i] = fromString(tags[i]);
        }
        return addressTags;
    }

    public static String[] toString(AddressTag[] tags) {
        String[] addressTags = new String[tags.length];
        for (int i = 0; i < tags.length; i++) {
            addressTags[i] = tags[i].name();
        }
        return addressTags;
    }

    public static String[] toString(AddressTag tag) {
        return new String[]{tag.name()};
    }






}
