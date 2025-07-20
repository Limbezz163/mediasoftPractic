package com.restaurant.reviewrestaurant.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RateResponseDTO {
    Long visitorId;
    Long restaurantId;
    Integer rating;
    String reviewText;
}