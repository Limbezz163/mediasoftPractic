package com.restaurant.reviewrestaurant.dto;

import com.restaurant.reviewrestaurant.enums.CuisineType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
@Schema(description = "DTO с информацией о ресторане")
public class RestaurantResponseDTO {
    @Schema(description = "Уникальный идентификатор ресторана", example = "1")
    Long id;

    @Schema(description = "Название ресторана", example = "Гастрономический рай")
    String name;

    @Schema(description = "Описание ресторана", example = "Ресторан высокой кухни с авторскими блюдами")
    String description;

    @Schema(description = "Тип кухни", example = "ITALIAN")
    CuisineType cuisineType;

    @Schema(description = "Средний чек на человека", example = "1500.50")
    BigDecimal averagePrice;

    @Schema(description = "Рейтинг ресторана (от 0 до 5)", example = "4.5")
    BigDecimal rating;
}