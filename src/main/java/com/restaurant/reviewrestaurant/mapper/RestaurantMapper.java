package com.restaurant.reviewrestaurant.mapper;

import com.restaurant.reviewrestaurant.dto.RestaurantRequestDTO;
import com.restaurant.reviewrestaurant.dto.RestaurantResponseDTO;
import com.restaurant.reviewrestaurant.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.math.BigDecimal;
@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", expression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(source = "description", target = "description")
    Restaurant toEntity(RestaurantRequestDTO dto);

    @Mapping(source = "description", target = "description")
    RestaurantResponseDTO toResponseDTO(Restaurant entity);
}