package com.restaurant.reviewrestaurant.Repositories;

import com.restaurant.reviewrestaurant.entity.Rate;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RateRepository {
    private final List<Rate> rates = new ArrayList<>();

    public void save(Rate rate) {
        rates.add(rate);
    }

    public List<Rate> findAll() {
        return new ArrayList<>(rates);
    }

    public void remove(long restaurantId, long visitorId) {
        rates.removeIf(rate ->
                rate.getRestaurantId().equals(restaurantId) &&
                        rate.getVisitorId().equals(visitorId)
        );
    }

    public Rate findById(long visitorId, long restaurantId) {
        return rates.stream()
                .filter(rate -> rate.getVisitorId().equals(visitorId) &&
                        rate.getRestaurantId().equals(restaurantId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Оценка не найдена для visitorId: " + visitorId +
                                " и restaurantId: " + restaurantId));
    }

    public void update(long restaurantId, long visitorId, Rate rate) {
        Rate oldRate = rates.stream()
                .filter(r -> r.getVisitorId().equals(visitorId) &&
                        r.getRestaurantId().equals(restaurantId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Отзыв не найден для visitorId: " + visitorId +
                                " и restaurantId: " + restaurantId));

        oldRate.setRating(rate.getRating());
        oldRate.setReviewText(rate.getReviewText());
    }
    public boolean existsByVisitorIdAndRestaurantId(Long visitorId, Long restaurantId) {
        return rates.stream()
                .anyMatch(rate -> rate.getVisitorId().equals(visitorId) &&
                        rate.getRestaurantId().equals(restaurantId));
    }
}