package com.restaurant.reviewrestaurant.Services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.restaurant.reviewrestaurant.Repositories.RestaurantRepository;
import com.restaurant.reviewrestaurant.entity.Restaurant;

import java.util.List;
@RequiredArgsConstructor
@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public void save(Restaurant restaurant) {
        if(restaurantRepository.findAll().contains(restaurant)) {
            System.out.println("Такой ресторан уже существует");
        } else {
            restaurantRepository.save(restaurant);
        }
    }

    public void remove(Restaurant restaurant) {
        if (!restaurantRepository.findAll().contains(restaurant)) {
            throw new IllegalArgumentException("Ресторан не найден");
        }
        restaurantRepository.remove(restaurant);
    }

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public Restaurant findById(Long id) {
        return restaurantRepository.findAll().stream()
                .filter(restaurant -> restaurant.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Не найден ресторан с id " + id));
    }
}