package com.restaurant.reviewrestaurant.controller;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.reviewrestaurant.dto.RateRequestDTO;
import com.restaurant.reviewrestaurant.dto.RateResponseDTO;
import com.restaurant.reviewrestaurant.dto.RateUpdateDTO;
import com.restaurant.reviewrestaurant.Services.RateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RateController.class)
class RateControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RateService rateService;

    @Test
    void createRate_ShouldReturnCreatedRate() throws Exception {
        // Given
        RateRequestDTO requestDTO = RateRequestDTO.builder()
                .visitorId(1L)
                .restaurantId(1L)
                .rating(5)
                .reviewText("Отличный ресторан!")
                .build();

        RateResponseDTO responseDTO = RateResponseDTO.builder()
                .visitorId(1L)
                .restaurantId(1L)
                .rating(5)
                .reviewText("Отличный ресторан!")
                .build();

        given(rateService.save(any(RateRequestDTO.class))).willReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/api/rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.visitorId").value(1L))
                .andExpect(jsonPath("$.restaurantId").value(1L))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.reviewText").value("Отличный ресторан!"));

        verify(rateService).save(any(RateRequestDTO.class));
    }

    @Test
    void getAllRates_ShouldReturnAllRates() throws Exception {
        // Given
        RateResponseDTO responseDTO = RateResponseDTO.builder()
                .visitorId(1L)
                .restaurantId(1L)
                .rating(5)
                .reviewText("Отличный ресторан!")
                .build();

        List<RateResponseDTO> rates = Collections.singletonList(responseDTO);

        given(rateService.findAll()).willReturn(rates);

        // When & Then
        mockMvc.perform(get("/api/rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].visitorId").value(1L))
                .andExpect(jsonPath("$[0].restaurantId").value(1L))
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$[0].reviewText").value("Отличный ресторан!"));

        verify(rateService).findAll();
    }

    @Test
    void getRatesByRestaurant_ShouldReturnPagedRates() throws Exception {
        // Given
        RateResponseDTO responseDTO = RateResponseDTO.builder()
                .visitorId(1L)
                .restaurantId(1L)
                .rating(5)
                .reviewText("Отличный ресторан!")
                .build();

        Page<RateResponseDTO> pagedResponse = new PageImpl<>(Collections.singletonList(responseDTO));

        // Мокаем вызов с тремя параметрами
        given(rateService.getRatesByRestaurant(eq(1L), eq(0), eq(10))).willReturn(pagedResponse);

        // When & Then
        mockMvc.perform(get("/api/rates/restaurant/1?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].visitorId").value(1L))
                .andExpect(jsonPath("$.content[0].restaurantId").value(1L))
                .andExpect(jsonPath("$.content[0].rating").value(5))
                .andExpect(jsonPath("$.content[0].reviewText").value("Отличный ресторан!"));

        // Проверяем вызов с тремя параметрами
        verify(rateService).getRatesByRestaurant(eq(1L), eq(0), eq(10));
    }

    @Test
    void getRateById_ShouldReturnRate() throws Exception {
        // Given
        RateResponseDTO responseDTO = RateResponseDTO.builder()
                .visitorId(1L)
                .restaurantId(1L)
                .rating(5)
                .reviewText("Отличный ресторан!")
                .build();

        given(rateService.findRateById(1L, 1L)).willReturn(responseDTO);

        // When & Then
        mockMvc.perform(get("/api/rates/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitorId").value(1L))
                .andExpect(jsonPath("$.restaurantId").value(1L))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.reviewText").value("Отличный ресторан!"));

        verify(rateService).findRateById(1L, 1L);
    }

    @Test
    void updateRate_ShouldReturnUpdatedRate() throws Exception {
        // Given
        RateUpdateDTO updateDTO = RateUpdateDTO.builder()
                .rating(4)
                .reviewText("Хороший ресторан, но можно улучшить сервис")
                .build();

        RateResponseDTO responseDTO = RateResponseDTO.builder()
                .visitorId(1L)
                .restaurantId(1L)
                .rating(4)
                .reviewText("Хороший ресторан, но можно улучшить сервис")
                .build();

        given(rateService.update(eq(1L), eq(1L), any(RateUpdateDTO.class))).willReturn(responseDTO);

        // When & Then
        mockMvc.perform(put("/api/rates/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitorId").value(1L))
                .andExpect(jsonPath("$.restaurantId").value(1L))
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.reviewText").value("Хороший ресторан, но можно улучшить сервис"));

        verify(rateService).update(eq(1L), eq(1L), any(RateUpdateDTO.class));
    }

    @Test
    void deleteRate_ShouldReturnNoContent() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/rates/1/1"))
                .andExpect(status().isNoContent());

        verify(rateService).remove(1L, 1L);
    }

    @Test
    void createRate_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        RateRequestDTO invalidRequest = RateRequestDTO.builder()
                .visitorId(null)
                .restaurantId(null)
                .rating(6)
                .reviewText("")
                .build();

        // When & Then
        mockMvc.perform(post("/api/rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}