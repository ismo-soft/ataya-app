package com.ataya.company.enums;

public enum ProductCategory {
    ELECTRONICS,
    FASHION,
    HOME_APPLIANCES,
    BOOKS,
    GROCERIES,
    SPORTS,
    TOYS,
    BEAUTY,
    HEALTH,
    AUTOMOTIVE,
    TOOLS,
    OTHERS;

    // check if the category exist ignore case
    public static boolean isCategoryExist(String category) {
        for (ProductCategory c : ProductCategory.values()) {
            if (c.name().equalsIgnoreCase(category)) {
                return true;
            }
        }
        return false;
    }

    // to get the category by name
    public static ProductCategory getCategory(String category) {
        for (ProductCategory c : ProductCategory.values()) {
            if (c.name().equalsIgnoreCase(category)) {
                return c;
            }
        }
        return null;
    }

}
