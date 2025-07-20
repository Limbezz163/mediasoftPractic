package com.restaurant.reviewrestaurant.Services;

import com.restaurant.reviewrestaurant.dto.RestaurantRequestDTO;
import com.restaurant.reviewrestaurant.dto.RestaurantResponseDTO;
import com.restaurant.reviewrestaurant.entity.Restaurant;
import com.restaurant.reviewrestaurant.mapper.RestaurantMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.restaurant.reviewrestaurant.Repositories.RestaurantRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private long idCounter = 0;

    public RestaurantResponseDTO save(RestaurantRequestDTO requestDTO) {
        Restaurant restaurant = restaurantMapper.toEntity(requestDTO);
        restaurant.setId(++idCounter); // Устанавливаем ID перед сохранением
        restaurant.setRating(BigDecimal.ZERO);
        restaurantRepository.save(restaurant);
        return restaurantMapper.toResponseDTO(restaurant);
    }

    public void remove(long id) {
        if (restaurantRepository.findById(id) == null) {
            throw new EntityNotFoundException("Ресторан не найден");
        }
        restaurantRepository.remove(id);
    }

    public List<RestaurantResponseDTO> findAll() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public RestaurantResponseDTO findById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id);
        return restaurantMapper.toResponseDTO(restaurant);
    }

    public RestaurantResponseDTO update(Long id, RestaurantRequestDTO requestDTO) {
        Restaurant existing = restaurantRepository.findById(id);
        if (existing == null) {
            throw new EntityNotFoundException("Ресторан с ID " + id + " не найден");
        }

        Restaurant updated = restaurantMapper.toEntity(requestDTO);
        updated.setId(id); // Сохраняем оригинальный ID
        restaurantRepository.update(id, updated);

        return restaurantMapper.toResponseDTO(updated);
    }
}