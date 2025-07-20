package com.restaurant.reviewrestaurant.mapper;

import com.restaurant.reviewrestaurant.dto.VisitorRequestDTO;
import com.restaurant.reviewrestaurant.dto.VisitorResponseDTO;
import com.restaurant.reviewrestaurant.entity.Visitor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface VisitorMapper {

    // Преобразование из RequestDTO в Entity при создании
    @Mapping(target = "id", ignore = true)
    Visitor toEntity(VisitorRequestDTO dto);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "age", source = "age")
    @Mapping(target = "gender", source = "gender")
    VisitorResponseDTO toResponseDTO(Visitor entity);

    // Обновление Entity из RequestDTO
    @Mapping(target = "id", ignore = true) // ID не должен обновляться из DTO
    void updateEntityFromDto(VisitorRequestDTO dto, @MappingTarget Visitor entity);
}