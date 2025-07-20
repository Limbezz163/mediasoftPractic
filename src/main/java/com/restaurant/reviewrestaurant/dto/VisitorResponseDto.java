package com.restaurant.reviewrestaurant.dto;

import com.restaurant.reviewrestaurant.enums.Gender;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VisitorResponseDto {
    Long id;
    String name;
    Integer age;
    Gender gender;
}