package com.restaurant.reviewrestaurant.mapper;

import com.restaurant.reviewrestaurant.dto.RestaurantRequestDTO;
import com.restaurant.reviewrestaurant.dto.RestaurantResponseDTO;
import com.restaurant.reviewrestaurant.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", expression = "java(BigDecimal.ZERO)")
    @Mapping(source = "description", target = "description") // Явный маппинг description
    Restaurant toEntity(RestaurantRequestDTO dto);

    @Mapping(source = "description", target = "description") // Маппинг для response
    RestaurantResponseDTO toResponseDTO(Restaurant entity);
}