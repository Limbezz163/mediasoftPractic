package com.restaurant.reviewrestaurant.enums;

public enum CuisineType {
    EUROPEAN("Европейская"),
    CHINESE("Китайская"),
    JAPANESE("Японская"),
    RUSSIAN("Русская");

    private final String displayName;
    CuisineType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}