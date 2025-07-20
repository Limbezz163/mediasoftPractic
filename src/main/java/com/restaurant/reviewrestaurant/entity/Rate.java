package com.restaurant.reviewrestaurant.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.Objects;


@Data
@AllArgsConstructor
public class Rate {
    @NonNull private  Long visitorId;
    @NonNull private Long restaurantId;
    @NonNull private Integer rating;
    private String reviewText;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate = (Rate) o;
        return visitorId.equals(rate.visitorId) &&
                restaurantId.equals(rate.restaurantId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(visitorId, restaurantId);
    }

}