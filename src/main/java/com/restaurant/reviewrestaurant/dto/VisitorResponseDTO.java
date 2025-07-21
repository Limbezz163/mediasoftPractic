package com.restaurant.reviewrestaurant.dto;

import com.restaurant.reviewrestaurant.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(description = "DTO для ответа с информацией о посетителе")
public class VisitorResponseDTO {
    @Schema(description = "Уникальный идентификатор посетителя", example = "1")
    Long id;

    @Schema(description = "Имя посетителя", example = "Иван Иванов")
    String name;

    @Schema(description = "Возраст посетителя", example = "25")
    Integer age;

    @Schema(description = "Пол посетителя", example = "MALE")
    Gender gender;
}