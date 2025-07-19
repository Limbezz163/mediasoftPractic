package com.restaurant.reviewrestaurant.enums;

public enum Gender {
    MALE,
    FEMALE;

    public static Gender fromString(String value) {
        if (value == null) {
            return null;
        }
        return Gender.valueOf(value.toUpperCase());
    }
}