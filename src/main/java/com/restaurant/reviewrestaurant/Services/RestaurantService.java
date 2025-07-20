package com.restaurant.reviewrestaurant.Services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.restaurant.reviewrestaurant.Repositories.RestaurantRepository;
import com.restaurant.reviewrestaurant.entity.Restaurant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private long idCounter = 0;

    public void save(Restaurant restaurant) {
                restaurant.setId(++idCounter);
                restaurantRepository.save(restaurant);
    }
    public void remove(long id) {
        if (restaurantRepository.findById(id) == null) {
            throw new EntityNotFoundException("Ресторан не найден");
        }
        restaurantRepository.remove(id);
    }

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public Restaurant findById(Long id) {
        return restaurantRepository.findById(id);
    }
    public void update(Long id, Restaurant updatedRestaurant) {
        Restaurant existing = restaurantRepository.findById(id);
        if (existing == null) {
            throw new EntityNotFoundException("Ресторан с ID " + id + " не найден");
        }
        restaurantRepository.update(id, updatedRestaurant);
    }
}