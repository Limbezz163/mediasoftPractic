package com.restaurant.reviewrestaurant.dto;

import com.restaurant.reviewrestaurant.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(description = "DTO для создания или обновления посетителя")
public class VisitorRequestDTO {

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно быть длиной от 2-ух до 50-ти симоволов")
    @Schema(description = "Имя посетителя", example = "Иван Иванов", requiredMode = Schema.RequiredMode.REQUIRED)
    String name;

    @NotNull(message = "Возраст не должен быть пустым")
    @Min(value = 14, message = "Возраст должен быть больше 14 лет")
    @Max(value = 100, message = "Возраст не должен превышать 100 лет")
    @Schema(description = "Возраст посетителя", example = "25", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer age;

    @NotNull(message = "Пол не должен быть пустым")
    @Schema(description = "Пол посетителя", example = "MALE", requiredMode = Schema.RequiredMode.REQUIRED)
    Gender gender;
}