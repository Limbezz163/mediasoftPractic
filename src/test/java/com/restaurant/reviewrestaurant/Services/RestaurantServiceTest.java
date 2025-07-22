package com.restaurant.reviewrestaurant.Services;

import com.restaurant.reviewrestaurant.dto.RestaurantRequestDTO;
import com.restaurant.reviewrestaurant.dto.RestaurantResponseDTO;
import com.restaurant.reviewrestaurant.entity.Restaurant;
import com.restaurant.reviewrestaurant.enums.CuisineType;
import com.restaurant.reviewrestaurant.exception.DuplicateRateException;
import com.restaurant.reviewrestaurant.mapper.RestaurantMapper;
import com.restaurant.reviewrestaurant.Repositories.RestaurantRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void save_ShouldReturnSavedRestaurantResponseDTO() {
        RestaurantRequestDTO requestDTO = RestaurantRequestDTO.builder()
                .name("Test Restaurant")
                .description("Test Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .build();

        Restaurant restaurant = Restaurant.builder()
                .name("Test Restaurant")
                .description("Test Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.ZERO)
                .build();

        Restaurant savedRestaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .description("Test Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.ZERO)
                .build();

        RestaurantResponseDTO expectedResponse = RestaurantResponseDTO.builder()
                .id(1L)
                .name("Test Restaurant")
                .description("Test Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.ZERO)
                .build();

        when(restaurantMapper.toEntity(requestDTO)).thenReturn(restaurant);
        when(restaurantRepository.save(restaurant)).thenReturn(savedRestaurant);
        when(restaurantMapper.toResponseDTO(savedRestaurant)).thenReturn(expectedResponse);

        RestaurantResponseDTO result = restaurantService.save(requestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Restaurant", result.getName());
        assertEquals(BigDecimal.ZERO, result.getRating());
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void save_WithInvalidData_ShouldThrowConstraintViolationException() {
        // Пустое название
        RestaurantRequestDTO invalidRequestDTO = RestaurantRequestDTO.builder()
                .name("") // Нарушение @NotBlank
                .description("Test Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .build();

        Restaurant restaurant = Restaurant.builder()
                .name("")
                .description("Test Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.ZERO)
                .build();

        when(restaurantMapper.toEntity(invalidRequestDTO)).thenReturn(restaurant);
        when(restaurantRepository.save(restaurant)).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class, () -> restaurantService.save(invalidRequestDTO));
    }

    @Test
    void save_WithNegativeRating_ShouldThrowConstraintViolationException() {
        RestaurantRequestDTO invalidRequestDTO = RestaurantRequestDTO.builder()
                .name("Test Restaurant")
                .description("Test Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.valueOf(-1.0)) // Нарушение @DecimalMin
                .build();

        Restaurant restaurant = Restaurant.builder()
                .name("Test Restaurant")
                .description("Test Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.valueOf(-1.0))
                .build();

        when(restaurantMapper.toEntity(invalidRequestDTO)).thenReturn(restaurant);
        when(restaurantRepository.save(restaurant)).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class, () -> restaurantService.save(invalidRequestDTO));
    }

    @Test
    void remove_WhenRestaurantExists_ShouldDeleteRestaurant() {
        long restaurantId = 1L;
        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);

        restaurantService.remove(restaurantId);

        verify(restaurantRepository).deleteById(restaurantId);
    }

    @Test
    void remove_WhenRestaurantNotExists_ShouldThrowException() {
        long restaurantId = 1L;
        when(restaurantRepository.existsById(restaurantId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> restaurantService.remove(restaurantId));
        verify(restaurantRepository, never()).deleteById(restaurantId);
    }

    @Test
    void findAll_ShouldReturnAllRestaurants() {
        Restaurant restaurant1 = Restaurant.builder()
                .id(1L)
                .name("Restaurant 1")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.valueOf(4.5))
                .build();

        Restaurant restaurant2 = Restaurant.builder()
                .id(2L)
                .name("Restaurant 2")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(2000))
                .rating(BigDecimal.valueOf(3.8))
                .build();

        RestaurantResponseDTO response1 = RestaurantResponseDTO.builder()
                .id(1L)
                .name("Restaurant 1")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.valueOf(4.5))
                .build();

        RestaurantResponseDTO response2 = RestaurantResponseDTO.builder()
                .id(2L)
                .name("Restaurant 2")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(2000))
                .rating(BigDecimal.valueOf(3.8))
                .build();

        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant1, restaurant2));
        when(restaurantMapper.toResponseDTO(restaurant1)).thenReturn(response1);
        when(restaurantMapper.toResponseDTO(restaurant2)).thenReturn(response2);

        List<RestaurantResponseDTO> result = restaurantService.findAll();

        assertEquals(2, result.size());
        assertEquals("Restaurant 1", result.get(0).getName());
        assertEquals(CuisineType.ITALIAN, result.get(0).getCuisineType());
        assertEquals(BigDecimal.valueOf(1500), result.get(0).getAveragePrice());
        assertEquals("Restaurant 2", result.get(1).getName());
        assertEquals(CuisineType.ITALIAN, result.get(1).getCuisineType());
        assertEquals(BigDecimal.valueOf(2000), result.get(1).getAveragePrice());
    }

    @Test
    void findById_WhenRestaurantExists_ShouldReturnRestaurant() {
        Long restaurantId = 1L;
        Restaurant restaurant = Restaurant.builder()
                .id(restaurantId)
                .name("Test Restaurant")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.valueOf(4.5))
                .build();

        RestaurantResponseDTO expectedResponse = RestaurantResponseDTO.builder()
                .id(restaurantId)
                .name("Test Restaurant")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.valueOf(4.5))
                .build();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantMapper.toResponseDTO(restaurant)).thenReturn(expectedResponse);

        RestaurantResponseDTO result = restaurantService.findById(restaurantId);

        assertNotNull(result);
        assertEquals(restaurantId, result.getId());
        assertEquals("Test Restaurant", result.getName());
        assertEquals(CuisineType.ITALIAN, result.getCuisineType());
        assertEquals(BigDecimal.valueOf(1500), result.getAveragePrice());
        assertEquals(BigDecimal.valueOf(4.5), result.getRating());
    }

    @Test
    void findById_WhenRestaurantNotExists_ShouldThrowException() {
        Long restaurantId = 1L;
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> restaurantService.findById(restaurantId));
    }

    @Test
    void update_WhenRestaurantExists_ShouldReturnUpdatedRestaurant() {
        Long restaurantId = 1L;
        RestaurantRequestDTO requestDTO = RestaurantRequestDTO.builder()
                .name("Updated Name")
                .description("Updated Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(2000))
                .build();

        Restaurant existing = Restaurant.builder()
                .id(restaurantId)
                .name("Old Name")
                .description("Old Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.valueOf(4.5))
                .build();

        Restaurant updated = Restaurant.builder()
                .id(restaurantId)
                .name("Updated Name")
                .description("Updated Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(2000))
                .rating(BigDecimal.valueOf(4.5))
                .build();

        RestaurantResponseDTO expectedResponse = RestaurantResponseDTO.builder()
                .id(restaurantId)
                .name("Updated Name")
                .description("Updated Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(2000))
                .rating(BigDecimal.valueOf(4.5))
                .build();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(existing));
        when(restaurantMapper.toEntity(requestDTO)).thenReturn(updated);
        when(restaurantRepository.save(updated)).thenReturn(updated);
        when(restaurantMapper.toResponseDTO(updated)).thenReturn(expectedResponse);

        RestaurantResponseDTO result = restaurantService.update(restaurantId, requestDTO);

        assertNotNull(result);
        assertEquals(restaurantId, result.getId());
        assertEquals("Updated Name", result.getName());
        assertEquals(CuisineType.ITALIAN, result.getCuisineType());
    }

    @Test
    void update_WithInvalidData_ShouldThrowConstraintViolationException() {
        Long restaurantId = 1L;
        RestaurantRequestDTO invalidRequestDTO = RestaurantRequestDTO.builder()
                .name("U") // Нарушение @Size(min = 2)
                .description("Updated Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(2000))
                .build();

        Restaurant existing = Restaurant.builder()
                .id(restaurantId)
                .name("Old Name")
                .description("Old Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.valueOf(4.5))
                .build();

        Restaurant updated = Restaurant.builder()
                .id(restaurantId)
                .name("U")
                .description("Updated Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(2000))
                .rating(BigDecimal.valueOf(4.5))
                .build();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(existing));
        when(restaurantMapper.toEntity(invalidRequestDTO)).thenReturn(updated);
        when(restaurantRepository.save(updated)).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class,
                () -> restaurantService.update(restaurantId, invalidRequestDTO));
    }

    @Test
    void update_WhenRestaurantNotExists_ShouldThrowException() {
        Long restaurantId = 1L;
        RestaurantRequestDTO requestDTO = RestaurantRequestDTO.builder()
                .name("Updated Name")
                .build();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> restaurantService.update(restaurantId, requestDTO));
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void getRestaurantsWithMinRating_ShouldReturnFilteredRestaurants() {
        BigDecimal minRating = BigDecimal.valueOf(4.0);
        Restaurant restaurant1 = Restaurant.builder()
                .id(1L)
                .name("Restaurant 1")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.valueOf(4.5))
                .build();

        Restaurant restaurant2 = Restaurant.builder()
                .id(2L)
                .name("Restaurant 2")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(2000))
                .rating(BigDecimal.valueOf(4.2))
                .build();

        RestaurantResponseDTO response1 = RestaurantResponseDTO.builder()
                .id(1L)
                .name("Restaurant 1")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.valueOf(4.5))
                .build();

        RestaurantResponseDTO response2 = RestaurantResponseDTO.builder()
                .id(2L)
                .name("Restaurant 2")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(2000))
                .rating(BigDecimal.valueOf(4.2))
                .build();

        when(restaurantRepository.findByRatingGreaterThanEqual(minRating))
                .thenReturn(List.of(restaurant1, restaurant2));
        when(restaurantMapper.toResponseDTO(restaurant1)).thenReturn(response1);
        when(restaurantMapper.toResponseDTO(restaurant2)).thenReturn(response2);

        List<RestaurantResponseDTO> result = restaurantService.getRestaurantsWithMinRating(minRating);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(r -> r.getRating().compareTo(minRating) >= 0));
    }

    @Test
    void getRestaurantsWithMinRatingJpql_ShouldReturnFilteredRestaurants() {
        BigDecimal minRating = BigDecimal.valueOf(4.0);
        Restaurant restaurant1 = Restaurant.builder()
                .id(1L)
                .name("Restaurant 1")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.valueOf(4.5))
                .build();

        Restaurant restaurant2 = Restaurant.builder()
                .id(2L)
                .name("Restaurant 2")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(2000))
                .rating(BigDecimal.valueOf(4.2))
                .build();

        RestaurantResponseDTO response1 = RestaurantResponseDTO.builder()
                .id(1L)
                .name("Restaurant 1")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.valueOf(4.5))
                .build();

        RestaurantResponseDTO response2 = RestaurantResponseDTO.builder()
                .id(2L)
                .name("Restaurant 2")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(2000))
                .rating(BigDecimal.valueOf(4.2))
                .build();

        when(restaurantRepository.findRestaurantsWithMinRating(minRating))
                .thenReturn(List.of(restaurant1, restaurant2));
        when(restaurantMapper.toResponseDTO(restaurant1)).thenReturn(response1);
        when(restaurantMapper.toResponseDTO(restaurant2)).thenReturn(response2);

        List<RestaurantResponseDTO> result = restaurantService.getRestaurantsWithMinRatingJpql(minRating);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(r -> r.getRating().compareTo(minRating) >= 0));
    }

    @Test
    void save_WithDuplicateRestaurant_ShouldThrowDuplicateRateException() {
        RestaurantRequestDTO requestDTO = RestaurantRequestDTO.builder()
                .name("Test Restaurant")
                .description("Test Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .build();

        Restaurant restaurant = Restaurant.builder()
                .name("Test Restaurant")
                .description("Test Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.ZERO)
                .build();

        when(restaurantMapper.toEntity(requestDTO)).thenReturn(restaurant);
        when(restaurantRepository.save(restaurant)).thenThrow(DuplicateRateException.class);

        assertThrows(DuplicateRateException.class, () -> restaurantService.save(requestDTO));
    }
}