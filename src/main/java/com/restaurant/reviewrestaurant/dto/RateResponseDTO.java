package com.restaurant.reviewrestaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(description = "DTO для ответа с информацией об оценке ресторана")
public class RateResponseDTO {
    @Schema(description = "Уникальный идентификатор посетителя", example = "1")
    Long visitorId;

    @Schema(description = "Уникальный идентификатор ресторана", example = "1")
    Long restaurantId;

    @Schema(description = "Оценка ресторана (от 1 до 5)", example = "5")
    Integer rating;

    @Schema(description = "Текст отзыва", example = "Отличный ресторан, рекомендую!")
    String reviewText;
}