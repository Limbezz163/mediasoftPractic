package com.restaurant.reviewrestaurant.Repositories;

import com.restaurant.reviewrestaurant.entity.Rate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface RateRepository extends JpaRepository<Rate, Rate.RateId> {

    @Query("SELECT r FROM Rate r WHERE r.visitorId = :visitorId AND r.restaurantId = :restaurantId")
    Optional<Rate> findByVisitorIdAndRestaurantId(
            @Param("visitorId") Long visitorId,
            @Param("restaurantId") Long restaurantId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Rate r WHERE r.visitorId = :visitorId AND r.restaurantId = :restaurantId")
    void deleteByVisitorIdAndRestaurantId(
            @Param("visitorId") Long visitorId,
            @Param("restaurantId") Long restaurantId);

    boolean existsByVisitorIdAndRestaurantId(Long visitorId, Long restaurantId);

    @Query("SELECT r FROM Rate r WHERE r.restaurantId = :restaurantId")
    List<Rate> findAllByRestaurantId(@Param("restaurantId") Long restaurantId);

    Page<Rate> findAllByRestaurantId(Long restaurantId, Pageable pageable);

    @Query("SELECT r FROM Rate r WHERE r.restaurantId = :restaurantId ORDER BY r.rating DESC")
    List<Rate> findTopRatedByRestaurantId(
            @Param("restaurantId") Long restaurantId,
            Pageable pageable);
}