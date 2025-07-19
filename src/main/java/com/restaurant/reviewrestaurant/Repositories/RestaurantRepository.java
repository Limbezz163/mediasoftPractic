package com.restaurant.reviewrestaurant.Repositories;

import com.restaurant.reviewrestaurant.entity.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RestaurantRepository {
    private final List<Restaurant> restaurants= new ArrayList<>();

    public void save(Restaurant restaurant) {restaurants.add(restaurant);}

    public List<Restaurant> findAll() {return this.restaurants;}

    public void remove(Restaurant restaurant) {restaurants.remove(restaurant);}

}
