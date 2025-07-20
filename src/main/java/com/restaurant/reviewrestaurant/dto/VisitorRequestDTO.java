package com.restaurant.reviewrestaurant.dto;

import com.restaurant.reviewrestaurant.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VisitorRequestDTO {
    @NotBlank(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно быть длиной от 2-ух до 50-ти симоволов")
    String name;

    @NotNull(message = "Возраст не должен быть пустым")
    @Min(value = 14, message = "Возраст должен быть больше 14 лет")
    @Max(value = 100, message = "Возраст не должен превышать 100 лет")
    Integer age;

    @NotNull(message = "Пол не должен быть пустым")
    Gender gender;
}