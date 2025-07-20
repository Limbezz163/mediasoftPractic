package com.restaurant.reviewrestaurant.Services;

import com.restaurant.reviewrestaurant.Repositories.RateRepository;
import com.restaurant.reviewrestaurant.Repositories.RestaurantRepository;
import com.restaurant.reviewrestaurant.Repositories.VisitorRepository;
import com.restaurant.reviewrestaurant.entity.Rate;
import com.restaurant.reviewrestaurant.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service

public class RateService {
    private final RateRepository rateRepository;
    private final RestaurantRepository restaurantRepository;
    private final VisitorRepository visitorRepository;

    @Autowired
    public RateService(RateRepository rateRepository,
                       RestaurantRepository restaurantRepository,
                       VisitorRepository visitorRepository) {
        this.rateRepository = rateRepository;
        this.restaurantRepository = restaurantRepository;
        this.visitorRepository = visitorRepository;
    }

    public void save(Rate rate) {
        // Проверка существования ресторана и посетителя
        if (!restaurantRepository.findAll().stream()
                .anyMatch(r -> r.getId().equals(rate.getRestaurantId()))) {
            throw new IllegalArgumentException("Ресторан не найден");
        }

        if (!visitorRepository.findAll().stream()
                .anyMatch(v -> v.getId().equals(rate.getVisitorId()))) {
            throw new IllegalArgumentException("Посетитель не найден");
        }

        rateRepository.save(rate);
        updateRestaurantRating(rate.getRestaurantId());
    }


    public boolean remove(Rate rate) {
        boolean isRemoved = rateRepository.remove(rate);
        if (isRemoved) {
            updateRestaurantRating(rate.getRestaurantId());
        }
        return isRemoved;
    }


    public List<Rate> findAll() {
        return rateRepository.findAll();
    }


    public Rate findRateById(Long visitorId, Long restaurantId) {
        return rateRepository.findRateById(visitorId, restaurantId);
    }

    private void updateRestaurantRating(Long restaurantId) {
        List<Rate> restaurantRates = rateRepository.findAll()
                .stream()
                .filter(rate -> rate.getRestaurantId().equals(restaurantId))
                .toList();

        if (restaurantRates.isEmpty()) {
            return;
        }
        BigDecimal newRating = BigDecimal.valueOf(
                restaurantRates.stream()
                        .mapToInt(Rate::getRating)
                        .average()
                        .orElse(0.0)
        ).setScale(2, RoundingMode.HALF_UP);

        restaurantRepository.findAll().stream()
                .filter(r -> r.getId().equals(restaurantId))
                .findFirst()
                .ifPresent(existingRestaurant -> {

                    Restaurant updatedRestaurant = new Restaurant(
                            existingRestaurant.getId(),
                            existingRestaurant.getName(),
                            existingRestaurant.getDescription(),
                            existingRestaurant.getCuisineType(),
                            existingRestaurant.getAveragePrice(),
                            newRating
                    );
                    restaurantRepository.remove(existingRestaurant);
                    restaurantRepository.save(updatedRestaurant);
                });
    }
}
