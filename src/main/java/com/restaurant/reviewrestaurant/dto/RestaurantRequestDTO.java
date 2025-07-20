package com.restaurant.reviewrestaurant.dto;

import com.restaurant.reviewrestaurant.enums.CuisineType;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class RestaurantRequestDTO {
    @NotBlank(message = "Название не может быть пустым")
    @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
    String name;

    String description;

    @NotNull(message = "Тип кухни обязателен")
    CuisineType cuisineType;

    @NotNull(message = "Средняя цена обязательна")
    @DecimalMin(value = "0.0", message = "Цена не может быть отрицательной")
    BigDecimal averagePrice;

    @NotNull(message = "Рейтинг обязателен")
    @DecimalMin(value = "0.0", message = "Рейтинг не может быть отрицательным")
    @DecimalMax(value = "5.0", message = "Максимальный рейтинг - 5")
    BigDecimal rating;
}