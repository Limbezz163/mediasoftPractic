package com.restaurant.reviewrestaurant.Repositories;

import com.restaurant.reviewrestaurant.entity.Restaurant;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RestaurantRepository {

    private final List<Restaurant> restaurants= new ArrayList<>();


    public void save(Restaurant restaurant) {restaurants.add(restaurant);}

    public List<Restaurant> findAll() {return this.restaurants;}

    public Restaurant findById(Long id) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Не найден ресторан с id " + id));
    }

    public void remove(long id) {restaurants.removeIf(r -> r.getId().equals(id));}

    public void update(long id, Restaurant restaurant) {
        Restaurant oldRestaurant = findById(id);
        oldRestaurant.setName(restaurant.getName());
        oldRestaurant.setRating(restaurant.getRating());
        oldRestaurant.setDescription(restaurant.getDescription());
        oldRestaurant.setAveragePrice(restaurant.getAveragePrice());
    }
}