package com.restaurant.reviewrestaurant.dto;

import com.restaurant.reviewrestaurant.enums.CuisineType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
@Schema(description = "DTO для создания или обновления ресторана")
public class RestaurantRequestDTO {
    @NotBlank(message = "Название не может быть пустым")
    @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
    @Schema(description = "Название ресторана", example = "Гастрономический рай", minLength = 2, maxLength = 100)
    String name;

    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    @Schema(description = "Описание ресторана", example = "Ресторан высокой кухни с авторскими блюдами", maxLength = 500)
    String description;

    @NotNull(message = "Тип кухни обязателен")
    @Schema(description = "Тип кухни", example = "ITALIAN")
    CuisineType cuisineType;

    @NotNull(message = "Средняя цена обязательна")
    @DecimalMin(value = "0.0", message = "Цена не может быть отрицательной")
    @Schema(description = "Средний чек на человека", example = "1500.50")
    BigDecimal averagePrice;

    @Builder.Default
    @DecimalMin(value = "0.0", message = "Рейтинг не может быть отрицательным")
    @DecimalMax(value = "5.0", message = "Максимальный рейтинг - 5")
    @Schema(description = "Рейтинг ресторана (от 0 до 5)", example = "4.5", minimum = "0.0", maximum = "5.0")
    BigDecimal rating = BigDecimal.ZERO;
}