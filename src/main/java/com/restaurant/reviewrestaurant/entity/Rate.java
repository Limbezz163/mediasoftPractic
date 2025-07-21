package com.restaurant.reviewrestaurant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rates")
@IdClass(Rate.RateId.class)
public class Rate {
    @Id
    @Column(name = "visitor_id", nullable = false)
    private Long visitorId;

    @Id
    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @NonNull
    @Column(nullable = false)
    private Integer rating;

    @Column(length = 500)
    private String reviewText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visitor_id", insertable = false, updatable = false)
    private Visitor visitor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    private Restaurant restaurant;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RateId implements Serializable {
        private Long visitorId;
        private Long restaurantId;
    }

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