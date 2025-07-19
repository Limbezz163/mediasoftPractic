package com.restaurant.reviewrestaurant.entity;
import com.restaurant.reviewrestaurant.enums.CuisineType;
import lombok.Value;
import lombok.NonNull;
import java.math.BigDecimal;
import java.util.Objects;

@Value
public class Restaurant {
    @NonNull private final Long id;
    @NonNull private final String name;
    private final String description;
    @NonNull private final CuisineType cuisineType;
    @NonNull private final BigDecimal averagePrice;
    @NonNull private final BigDecimal rating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return id.equals(that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}