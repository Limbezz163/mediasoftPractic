package com.restaurant.reviewrestaurant.Services;

import com.restaurant.reviewrestaurant.dto.RestaurantRequestDTO;
import com.restaurant.reviewrestaurant.dto.RestaurantResponseDTO;
import com.restaurant.reviewrestaurant.entity.Restaurant;
import com.restaurant.reviewrestaurant.mapper.RestaurantMapper;
import com.restaurant.reviewrestaurant.Repositories.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    public RestaurantResponseDTO save(RestaurantRequestDTO requestDTO) {
        Restaurant restaurant = restaurantMapper.toEntity(requestDTO);
        restaurant.setRating(BigDecimal.ZERO);
        Restaurant saved = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponseDTO(saved);
    }

    public void remove(long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new EntityNotFoundException("Ресторан не найден");
        }
        restaurantRepository.deleteById(id);
    }

    public List<RestaurantResponseDTO> findAll() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public RestaurantResponseDTO findById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ресторан не найден"));
        return restaurantMapper.toResponseDTO(restaurant);
    }

    public RestaurantResponseDTO update(Long id, RestaurantRequestDTO requestDTO) {
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ресторан с ID " + id + " не найден"));

        Restaurant updated = restaurantMapper.toEntity(requestDTO);
        updated.setId(id);
        Restaurant saved = restaurantRepository.save(updated);
        return restaurantMapper.toResponseDTO(saved);
    }

    public List<RestaurantResponseDTO> getRestaurantsWithMinRating(BigDecimal minRating) {
        return restaurantRepository.findByRatingGreaterThanEqual(minRating)
                .stream()
                .map(restaurantMapper::toResponseDTO)
                .toList();
    }

    public List<RestaurantResponseDTO> getRestaurantsWithMinRatingJpql(BigDecimal minRating) {
        return restaurantRepository.findRestaurantsWithMinRating(minRating)
                .stream()
                .map(restaurantMapper::toResponseDTO)
                .toList();
    }
}