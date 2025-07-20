package com.restaurant.reviewrestaurant.mapper;

import com.restaurant.reviewrestaurant.dto.RateRequestDTO;
import com.restaurant.reviewrestaurant.dto.RateResponseDTO;
import com.restaurant.reviewrestaurant.dto.RateUpdateDTO;
import com.restaurant.reviewrestaurant.entity.Rate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RateMapper {
    Rate toEntity(RateRequestDTO dto);
    RateResponseDTO toResponseDTO(Rate entity);

    @Mapping(target = "visitorId", ignore = true)
    @Mapping(target = "restaurantId", ignore = true)
    void updateEntityFromDto(RateUpdateDTO dto, @MappingTarget Rate entity);
}