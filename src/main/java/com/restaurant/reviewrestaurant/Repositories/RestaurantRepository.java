package com.restaurant.reviewrestaurant.Repositories;

import com.restaurant.reviewrestaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByRatingGreaterThanEqual(BigDecimal minRating);

    @Query("SELECT r FROM Restaurant r WHERE r.rating >= :minRating ORDER BY r.rating DESC")
    List<Restaurant> findRestaurantsWithMinRating(
            @Param("minRating") BigDecimal minRating);
}