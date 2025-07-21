package com.restaurant.reviewrestaurant.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Перечисление для представления пола посетителя")
public enum Gender {
    @Schema(description = "Мужчина")
    MALE,

    @Schema(description = "Женщина")
    FEMALE;

    public static Gender fromString(String value) {
        if (value == null) {
            return null;
        }
        return Gender.valueOf(value.toUpperCase());
    }
}