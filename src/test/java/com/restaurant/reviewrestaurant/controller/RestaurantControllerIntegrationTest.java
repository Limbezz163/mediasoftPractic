package com.restaurant.reviewrestaurant.controller;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.reviewrestaurant.Services.RestaurantService;
import com.restaurant.reviewrestaurant.dto.RestaurantRequestDTO;
import com.restaurant.reviewrestaurant.dto.RestaurantResponseDTO;
import com.restaurant.reviewrestaurant.enums.CuisineType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RestaurantService restaurantService;

    @Test
    void createRestaurant_ShouldReturnCreatedRestaurant() throws Exception {
        // Given
        RestaurantRequestDTO requestDTO = RestaurantRequestDTO.builder()
                .name("Test Restaurant")
                .description("Test Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500.50))
                .rating(BigDecimal.valueOf(4.5))
                .build();

        RestaurantResponseDTO responseDTO = RestaurantResponseDTO.builder()
                .id(1L)
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .cuisineType(requestDTO.getCuisineType())
                .averagePrice(requestDTO.getAveragePrice())
                .rating(requestDTO.getRating())
                .build();

        given(restaurantService.save(any(RestaurantRequestDTO.class))).willReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(requestDTO.getName()))
                .andExpect(jsonPath("$.description").value(requestDTO.getDescription()))
                .andExpect(jsonPath("$.cuisineType").value(requestDTO.getCuisineType().name()))
                .andExpect(jsonPath("$.averagePrice").value(requestDTO.getAveragePrice().doubleValue()))
                .andExpect(jsonPath("$.rating").value(requestDTO.getRating().doubleValue()));
    }


    @Test
    void getAllRestaurants_ShouldReturnAllRestaurants() throws Exception {
        // Given
        RestaurantResponseDTO restaurant1 = RestaurantResponseDTO.builder()
                .id(1L)
                .name("Restaurant 1")
                .description("Description 1")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1000))
                .rating(BigDecimal.valueOf(4.5))
                .build();

        RestaurantResponseDTO restaurant2 = RestaurantResponseDTO.builder()
                .id(2L)
                .name("Restaurant 2")
                .description("Description 2")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.valueOf(4.8))
                .build();

        List<RestaurantResponseDTO> restaurants = Arrays.asList(restaurant1, restaurant2);
        given(restaurantService.findAll()).willReturn(restaurants);

        // When & Then
        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(restaurant1.getId()))
                .andExpect(jsonPath("$[1].id").value(restaurant2.getId()));
    }

    @Test
    void getRestaurantById_ShouldReturnRestaurant() throws Exception {
        // Given
        Long restaurantId = 1L;
        RestaurantResponseDTO responseDTO = RestaurantResponseDTO.builder()
                .id(restaurantId)
                .name("Test Restaurant")
                .description("Test Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(1500))
                .rating(BigDecimal.valueOf(4.5))
                .build();

        given(restaurantService.findById(restaurantId)).willReturn(responseDTO);

        // When & Then
        mockMvc.perform(get("/api/restaurants/{id}", restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDTO.getId()))
                .andExpect(jsonPath("$.name").value(responseDTO.getName()));
    }

    @Test
    void updateRestaurant_ShouldReturnUpdatedRestaurant() throws Exception {
        // Given
        Long restaurantId = 1L;
        RestaurantRequestDTO requestDTO = RestaurantRequestDTO.builder()
                .name("Updated Restaurant")
                .description("Updated Description")
                .cuisineType(CuisineType.ITALIAN)
                .averagePrice(BigDecimal.valueOf(2000))
                .rating(BigDecimal.valueOf(4.8))
                .build();

        RestaurantResponseDTO responseDTO = RestaurantResponseDTO.builder()
                .id(restaurantId)
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .cuisineType(requestDTO.getCuisineType())
                .averagePrice(requestDTO.getAveragePrice())
                .rating(requestDTO.getRating())
                .build();

        given(restaurantService.update(eq(restaurantId), any(RestaurantRequestDTO.class))).willReturn(responseDTO);

        // When & Then
        mockMvc.perform(put("/api/restaurants/{id}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restaurantId))
                .andExpect(jsonPath("$.name").value(requestDTO.getName()))
                .andExpect(jsonPath("$.cuisineType").value(requestDTO.getCuisineType().name()));
    }

    @Test
    void deleteRestaurant_ShouldReturnNoContent() throws Exception {
        // Given
        Long restaurantId = 1L;
        doNothing().when(restaurantService).remove(restaurantId);

        // When & Then
        mockMvc.perform(delete("/api/restaurants/{id}", restaurantId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getRestaurantsWithMinRating_ShouldReturnFilteredRestaurants() throws Exception {
        // Given
        BigDecimal minRating = BigDecimal.valueOf(4.0);
        boolean useJpql = false;

        RestaurantResponseDTO restaurant1 = RestaurantResponseDTO.builder()
                .id(1L)
                .name("High Rating Restaurant")
                .rating(BigDecimal.valueOf(4.5))
                .build();

        List<RestaurantResponseDTO> restaurants = List.of(restaurant1);
        given(restaurantService.getRestaurantsWithMinRating(minRating)).willReturn(restaurants);

        // When & Then
        mockMvc.perform(get("/api/restaurants/with-rating")
                        .param("minRating", minRating.toString())
                        .param("useJpql", String.valueOf(useJpql)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].rating").value(4.5));
    }

    @Test
    void createRestaurant_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        RestaurantRequestDTO invalidRequest = RestaurantRequestDTO.builder()
                .name("")
                .description("Test Description")
                .cuisineType(null)
                .averagePrice(BigDecimal.valueOf(-100))
                .rating(BigDecimal.valueOf(6.0))
                .build();

        // When & Then
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}