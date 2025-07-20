
package com.restaurant.reviewrestaurant.mapper;

import com.restaurant.reviewrestaurant.dto.VisitorRequestDto;
import com.restaurant.reviewrestaurant.dto.VisitorResponseDto;
import com.restaurant.reviewrestaurant.entity.Visitor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VisitorMapper {
    @Mapping(target = "id", ignore = true)
    Visitor toEntity(VisitorRequestDto dto);
    VisitorResponseDto toResponseDTO(Visitor entity);

    @Mapping(target = "id", ignore = true)
    VisitorRequestDto toRequestDTO(Visitor entity);
}