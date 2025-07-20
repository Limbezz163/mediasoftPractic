package com.restaurant.reviewrestaurant.entity;
import com.restaurant.reviewrestaurant.enums.CuisineType;
import lombok.Value;
import lombok.NonNull;
import java.math.BigDecimal;
import java.util.Objects;

@Value
public class Restaurant {
    @NonNull   Long id;
    @NonNull private  String name;
    private  String description;
    @NonNull private  CuisineType cuisineType;
    @NonNull private  BigDecimal averagePrice;
    @NonNull private  BigDecimal rating;

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