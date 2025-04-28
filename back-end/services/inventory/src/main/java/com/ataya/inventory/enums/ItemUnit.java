package com.ataya.inventory.enums;


import lombok.Getter;

@Getter
public enum ItemUnit {
    KG("Kilogram"),
    G("Gram"),
    L("Liter"),
    ML("Milliliter"),
    M("Meter"),
    CM("Centimeter"),
    MM("Millimeter"),
    KM("Kilometer"),
    HECTARE("Hectare"),
    ACRE("Acre"),
    SQM("Square Meter"),
    SQFT("Square Foot"),
    CUBIC_METER("Cubic Meter"),
    CUBIC_FEET("Cubic Feet"),
    PIECE("Piece"),
    DOZEN("Dozen"),
    PACK("Pack"),
    BOTTLE("Bottle"),
    BOX("Box"),
    CARTON("Carton"),
    PALLET("Pallet"),
    ROLL("Roll"),
    SHEET("Sheet"),
    SET("Set"),
    PACKET("Packet"),
    TUBE("Tube"),
    BAG("Bag"),
    JAR("Jar"),
    CAN("Can"),
    TIN("Tin"),
    POUND("Pound"),
    OUNCE("Ounce"),
    GRAM_PER_LITER("Gram per Liter");

    private final String displayName;

    ItemUnit(String displayName) {
        this.displayName = displayName;
    }

     // check if unit exists ignoring case
    public static boolean isValidUnit(String unit) {
        for (ItemUnit itemUnit : ItemUnit.values()) {
            if (itemUnit.name().equalsIgnoreCase(unit)) {
                return true;
            }
        }
        return false;
    }
    // get unit by name ignoring case
    public static ItemUnit getUnit(String unit) {
        for (ItemUnit itemUnit : ItemUnit.values()) {
            if (itemUnit.name().equalsIgnoreCase(unit)) {
                return itemUnit;
            }
        }
        return null;
    }

}
