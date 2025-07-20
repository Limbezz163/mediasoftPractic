package com.restaurant.reviewrestaurant.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RateRequestDTO {
    @NotNull(message = "ID посетителя обязательно")
    Long visitorId;

    @NotNull(message = "ID ресторана обязательно")
    Long restaurantId;

    @NotNull(message = "Оценка обязательна")
    @Min(value = 1, message = "Оценка должна быть от 1 до 5")
    @Max(value = 5, message = "Оценка должна быть от 1 до 5")
    Integer rating;

    @Size(max = 500, message = "Отзыв не должен превышать 500 символов")
    String reviewText;
}