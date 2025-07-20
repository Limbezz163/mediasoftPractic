package com.restaurant.reviewrestaurant.Repositories;
import com.restaurant.reviewrestaurant.entity.Rate;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
@Repository
public class RateRepository {
    private final List<Rate> rates= new ArrayList<>();

    public void save(Rate rate) {rates.add(rate);}

    public List<Rate> findAll() {
        return rates;
    }

    public void remove(long restaurantId, long visitorId) {
        rates.removeIf(rate ->
                rate.getRestaurantId() == restaurantId &&
                        rate.getVisitorId() == visitorId
        );
    }

    public Rate findById(long visitorId, long restaurantId) {
        return rates.stream().filter(review -> (review.getVisitorId().equals(visitorId)) && review.getRestaurantId().equals(restaurantId))
                .findFirst()
                .orElse(null);
    }

    public void update(long restaurantId, long visitorId, Rate rate) {
        Rate oldRate = findById(restaurantId, visitorId);
        oldRate.setRating(rate.getRating());
        oldRate.setReviewText(rate.getReviewText());
    }

}
