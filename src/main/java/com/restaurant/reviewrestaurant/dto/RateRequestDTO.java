package com.restaurant.reviewrestaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(description = "DTO для создания новой оценки ресторана")
public class RateRequestDTO {
    @NotNull(message = "ID посетителя обязательно")
    @Schema(description = "Уникальный идентификатор посетителя", example = "1")
    Long visitorId;

    @NotNull(message = "ID ресторана обязательно")
    @Schema(description = "Уникальный идентификатор ресторана", example = "1")
    Long restaurantId;

    @NotNull(message = "Оценка обязательна")
    @Min(value = 1, message = "Оценка должна быть от 1 до 5")
    @Max(value = 5, message = "Оценка должна быть от 1 до 5")
    @Schema(description = "Оценка ресторана (от 1 до 5)", example = "5")
    Integer rating;

    @Size(max = 500, message = "Отзыв не должен превышать 500 символов")
    @Schema(description = "Текст отзыва", example = "Отличный ресторан, рекомендую!", maxLength = 500)
    String reviewText;
}