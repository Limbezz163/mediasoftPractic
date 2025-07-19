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

    public boolean remove(Rate rate){return rates.remove(rate);}

    public Rate findRateById(long visitorId, long restaurantId) {
        return rates.stream().filter(review -> (review.getVisitorId().equals(visitorId)) && review.getRestaurantId().equals(restaurantId))
                .findFirst()
                .orElse(null);
    }
}
