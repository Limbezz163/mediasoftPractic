package com.restaurant.reviewrestaurant.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Тип кухни ресторана")
public enum CuisineType {
    @Schema(description = "Итальянская кухня", example = "ITALIAN")
    ITALIAN("Итальянская"),
    @Schema(description = "Китайская кухня", example = "CHINESE")
    CHINESE("Китайская"),
    @Schema(description = "Японская кухня", example = "JAPANESE")
    JAPANESE("Японская"),
    @Schema(description = "Русская кухня", example = "RUSSIAN")
    RUSSIAN("Русская");
    @Schema(description = "Отображаемое название кухни", example = "Итальянская")
    private final String displayName;
    CuisineType(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }

    @Schema(hidden = true)
    public String getName() {
        return name();
    }
}