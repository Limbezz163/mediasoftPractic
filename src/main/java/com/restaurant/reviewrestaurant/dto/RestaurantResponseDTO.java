package com.restaurant.reviewrestaurant.dto;

import com.restaurant.reviewrestaurant.enums.CuisineType;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class RestaurantResponseDTO {
    Long id;
    String name;
    String description;
    CuisineType cuisineType;
    BigDecimal averagePrice;
    BigDecimal rating;
}