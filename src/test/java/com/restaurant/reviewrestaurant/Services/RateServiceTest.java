package com.restaurant.reviewrestaurant.Services;

import com.restaurant.reviewrestaurant.Repositories.RateRepository;
import com.restaurant.reviewrestaurant.Repositories.RestaurantRepository;
import com.restaurant.reviewrestaurant.Repositories.VisitorRepository;
import com.restaurant.reviewrestaurant.dto.RateRequestDTO;
import com.restaurant.reviewrestaurant.dto.RateResponseDTO;
import com.restaurant.reviewrestaurant.dto.RateUpdateDTO;
import com.restaurant.reviewrestaurant.entity.Rate;
import com.restaurant.reviewrestaurant.entity.Restaurant;
import com.restaurant.reviewrestaurant.entity.Visitor;
import com.restaurant.reviewrestaurant.enums.Gender;
import com.restaurant.reviewrestaurant.exception.DuplicateRateException;
import com.restaurant.reviewrestaurant.exception.ResourceNotFoundException;
import com.restaurant.reviewrestaurant.mapper.RateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RateServiceTest {

    @Mock
    private RateRepository rateRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private VisitorRepository visitorRepository;
    @Mock
    private RateMapper rateMapper;

    @InjectMocks
    private RateService rateService;

    private RateRequestDTO rateRequestDTO;
    private RateUpdateDTO rateUpdateDTO;
    private Rate rate;
    private RateResponseDTO rateResponseDTO;
    private Restaurant restaurant;
    private Visitor visitor;

    @BeforeEach
    void setUp() {
        rateRequestDTO = RateRequestDTO.builder()
                .visitorId(1L)
                .restaurantId(1L)
                .rating(5)
                .reviewText("Great food!")
                .build();

        rateUpdateDTO = RateUpdateDTO.builder()
                .rating(4)
                .reviewText("Good, but could be better")
                .build();

        rate = new Rate();
        rate.setVisitorId(1L);
        rate.setRestaurantId(1L);
        rate.setRating(5);
        rate.setReviewText("Great food!");

        rateResponseDTO = RateResponseDTO.builder()
                .visitorId(1L)
                .restaurantId(1L)
                .rating(5)
                .reviewText("Great food!")
                .build();

        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setRating(BigDecimal.valueOf(4.5));

        visitor = Visitor.builder()
                .id(1L)
                .name("Test Visitor")
                .age(30)
                .gender(Gender.MALE)
                .build();
    }

    @Test
    void save_ShouldSaveRateWhenValidRequest() {
        when(restaurantRepository.existsById(1L)).thenReturn(true);
        when(visitorRepository.existsById(1L)).thenReturn(true);
        when(rateRepository.existsByVisitorIdAndRestaurantId(1L, 1L)).thenReturn(false);
        when(rateMapper.toEntity(rateRequestDTO)).thenReturn(rate);
        when(rateRepository.save(rate)).thenReturn(rate);
        when(rateMapper.toResponseDTO(rate)).thenReturn(rateResponseDTO);

        RateResponseDTO result = rateService.save(rateRequestDTO);

        assertEquals(rateResponseDTO, result);
        verify(rateRepository).save(rate);
    }

    @Test
    void save_ShouldThrowWhenRestaurantNotFound() {
        when(restaurantRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> rateService.save(rateRequestDTO));
        verify(rateRepository, never()).save(any());
    }

    @Test
    void save_ShouldThrowWhenVisitorNotFound() {
        when(restaurantRepository.existsById(1L)).thenReturn(true);
        when(visitorRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> rateService.save(rateRequestDTO));
        verify(rateRepository, never()).save(any());
    }

    @Test
    void save_ShouldThrowWhenRateAlreadyExists() {
        when(restaurantRepository.existsById(1L)).thenReturn(true);
        when(visitorRepository.existsById(1L)).thenReturn(true);
        when(rateRepository.existsByVisitorIdAndRestaurantId(1L, 1L)).thenReturn(true);

        assertThrows(DuplicateRateException.class, () -> rateService.save(rateRequestDTO));
        verify(rateRepository, never()).save(any());
    }

    @Test
    void remove_ShouldDeleteRateWhenExists() {
        when(rateRepository.existsByVisitorIdAndRestaurantId(1L, 1L)).thenReturn(true);
        doNothing().when(rateRepository).deleteByVisitorIdAndRestaurantId(1L, 1L);

        rateService.remove(1L, 1L);

        verify(rateRepository).deleteByVisitorIdAndRestaurantId(1L, 1L);
    }

    @Test
    void remove_ShouldThrowWhenRateNotFound() {
        when(rateRepository.existsByVisitorIdAndRestaurantId(1L, 1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> rateService.remove(1L, 1L));
        verify(rateRepository, never()).deleteByVisitorIdAndRestaurantId(any(), any());
    }

    @Test
    void findAll_ShouldReturnAllRates() {
        when(rateRepository.findAll()).thenReturn(List.of(rate));
        when(rateMapper.toResponseDTO(rate)).thenReturn(rateResponseDTO);

        List<RateResponseDTO> result = rateService.findAll();

        assertEquals(1, result.size());
        assertEquals(rateResponseDTO, result.get(0));
    }

    @Test
    void findRateById_ShouldReturnRateWhenExists() {
        when(rateRepository.findByVisitorIdAndRestaurantId(1L, 1L)).thenReturn(Optional.of(rate));
        when(rateMapper.toResponseDTO(rate)).thenReturn(rateResponseDTO);

        RateResponseDTO result = rateService.findRateById(1L, 1L);

        assertEquals(rateResponseDTO, result);
    }

    @Test
    void findRateById_ShouldThrowWhenNotFound() {
        when(rateRepository.findByVisitorIdAndRestaurantId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rateService.findRateById(1L, 1L));
    }

    @Test
    void update_ShouldUpdateRateWhenExists() {
        when(rateRepository.findByVisitorIdAndRestaurantId(1L, 1L)).thenReturn(Optional.of(rate));
        when(rateRepository.save(rate)).thenReturn(rate);
        when(rateMapper.toResponseDTO(rate)).thenReturn(rateResponseDTO);

        RateResponseDTO result = rateService.update(1L, 1L, rateUpdateDTO);

        assertEquals(rateResponseDTO, result);
        verify(rateMapper).updateEntityFromDto(rateUpdateDTO, rate);
    }

    @Test
    void update_ShouldThrowWhenNotFound() {
        when(rateRepository.findByVisitorIdAndRestaurantId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rateService.update(1L, 1L, rateUpdateDTO));
        verify(rateRepository, never()).save(any());
    }

    @Test
    void getRatesByRestaurant_ShouldReturnPagedRates() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Rate> ratePage = new PageImpl<>(List.of(rate), pageable, 1);
        when(rateRepository.findAllByRestaurantId(1L, pageable)).thenReturn(ratePage);
        when(rateMapper.toResponseDTO(rate)).thenReturn(rateResponseDTO);

        Page<RateResponseDTO> result = rateService.getRatesByRestaurant(1L, 0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals(rateResponseDTO, result.getContent().get(0));
    }
}