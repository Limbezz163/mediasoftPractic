package com.restaurant.reviewrestaurant.entity;
import com.restaurant.reviewrestaurant.enums.Gender;
import lombok.Value;
import lombok.NonNull;

import java.util.Objects;

@Value
public class Visitor {
    @NonNull private final Long id;
    private final String name;
    @NonNull private final Integer age;
    @NonNull private final Gender gender;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visitor visitor = (Visitor) o;
        return Objects.equals(id, visitor.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}