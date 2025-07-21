package com.restaurant.reviewrestaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(description = "DTO для обновления оценки ресторана")
public class RateUpdateDTO {
    @NotNull(message = "Оценка обязательна")
    @Min(value = 1, message = "Оценка должна быть от 1 до 5")
    @Max(value = 5, message = "Оценка должна быть от 1 до 5")
    @Schema(description = "Новая оценка ресторана (от 1 до 5)", example = "4")
    Integer rating;

    @Size(max = 500, message = "Отзыв не должен превышать 500 символов")
    @Schema(description = "Новый текст отзыва", example = "Хороший ресторан, но можно улучшить сервис", maxLength = 500)
    String reviewText;
}