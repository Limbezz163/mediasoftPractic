package com.restaurant.reviewrestaurant.mapper;

import com.restaurant.reviewrestaurant.dto.RestaurantRequestDTO;
import com.restaurant.reviewrestaurant.dto.RestaurantResponseDTO;
import com.restaurant.reviewrestaurant.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    @Mapping(target = "id", ignore = true)
    Restaurant toEntity(RestaurantRequestDTO dto);

    RestaurantResponseDTO toResponseDTO(Restaurant entity);
}