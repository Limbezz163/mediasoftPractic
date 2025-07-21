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
import com.restaurant.reviewrestaurant.mapper.RateMapper;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    void save_ShouldSaveRate_WhenValidRequest() {
        when(restaurantRepository.existsById(anyLong())).thenReturn(true);
        when(visitorRepository.existsById(anyLong())).thenReturn(true);
        when(rateRepository.existsByVisitorIdAndRestaurantId(anyLong(), anyLong())).thenReturn(false);
        when(rateMapper.toEntity(any(RateRequestDTO.class))).thenReturn(rate);
        when(rateRepository.save(any(Rate.class))).thenReturn(rate);
        when(rateMapper.toResponseDTO(any(Rate.class))).thenReturn(rateResponseDTO);

        RateResponseDTO result = rateService.save(rateRequestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getVisitorId());
        assertEquals(1L, result.getRestaurantId());
        assertEquals(5, result.getRating());
        assertEquals("Great food!", result.getReviewText());

        verify(rateRepository, times(1)).save(any(Rate.class));
        verify(restaurantRepository, times(1)).existsById(anyLong());
        verify(visitorRepository, times(1)).existsById(anyLong());
    }

    @Test
    void save_ShouldThrowException_WhenRestaurantNotFound() {
        when(restaurantRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> rateService.save(rateRequestDTO));
        verify(rateRepository, never()).save(any(Rate.class));
    }

    @Test
    void save_ShouldThrowException_WhenVisitorNotFound() {
        when(restaurantRepository.existsById(anyLong())).thenReturn(true);
        when(visitorRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> rateService.save(rateRequestDTO));
        verify(rateRepository, never()).save(any(Rate.class));
    }

    @Test
    void save_ShouldThrowException_WhenRateAlreadyExists() {
        when(restaurantRepository.existsById(anyLong())).thenReturn(true);
        when(visitorRepository.existsById(anyLong())).thenReturn(true);
        when(rateRepository.existsByVisitorIdAndRestaurantId(anyLong(), anyLong())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> rateService.save(rateRequestDTO));
        verify(rateRepository, never()).save(any(Rate.class));
    }

    @Test
    void save_ShouldUpdateRestaurantRating_WhenRateSaved() {
        when(restaurantRepository.existsById(anyLong())).thenReturn(true);
        when(visitorRepository.existsById(anyLong())).thenReturn(true);
        when(rateRepository.existsByVisitorIdAndRestaurantId(anyLong(), anyLong())).thenReturn(false);
        when(rateMapper.toEntity(any(RateRequestDTO.class))).thenReturn(rate);
        when(rateRepository.save(any(Rate.class))).thenReturn(rate);
        when(rateMapper.toResponseDTO(any(Rate.class))).thenReturn(rateResponseDTO);
        when(rateRepository.findAllByRestaurantId(anyLong())).thenReturn(List.of(rate));
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        rateService.save(rateRequestDTO);

        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void remove_ShouldDeleteRate_WhenExists() {
        when(rateRepository.existsByVisitorIdAndRestaurantId(anyLong(), anyLong())).thenReturn(true);
        doNothing().when(rateRepository).deleteByVisitorIdAndRestaurantId(anyLong(), anyLong());

        rateService.remove(1L, 1L);

        verify(rateRepository, times(1)).deleteByVisitorIdAndRestaurantId(anyLong(), anyLong());
    }

    @Test
    void remove_ShouldThrowException_WhenRateNotFound() {
        when(rateRepository.existsByVisitorIdAndRestaurantId(anyLong(), anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> rateService.remove(1L, 1L));
        verify(rateRepository, never()).deleteByVisitorIdAndRestaurantId(anyLong(), anyLong());
    }


    @Test
    void findAll_ShouldReturnAllRates() {
        when(rateRepository.findAll()).thenReturn(Collections.singletonList(rate));
        when(rateMapper.toResponseDTO(any(Rate.class))).thenReturn(rateResponseDTO);

        List<RateResponseDTO> result = rateService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(rateResponseDTO, result.get(0));
        verify(rateRepository, times(1)).findAll();
    }

    @Test
    void findRateById_ShouldReturnRate_WhenExists() {
        when(rateRepository.findByVisitorIdAndRestaurantId(anyLong(), anyLong()))
                .thenReturn(Optional.of(rate));
        when(rateMapper.toResponseDTO(any(Rate.class))).thenReturn(rateResponseDTO);

        RateResponseDTO result = rateService.findRateById(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getVisitorId());
        assertEquals(1L, result.getRestaurantId());
        verify(rateRepository, times(1)).findByVisitorIdAndRestaurantId(anyLong(), anyLong());
    }

    @Test
    void findRateById_ShouldThrowException_WhenNotFound() {
        when(rateRepository.findByVisitorIdAndRestaurantId(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rateService.findRateById(1L, 1L));
    }

    @Test
    void update_ShouldUpdateRate_WhenExists() {
        when(rateRepository.findByVisitorIdAndRestaurantId(anyLong(), anyLong()))
                .thenReturn(Optional.of(rate));
        when(rateRepository.save(any(Rate.class))).thenReturn(rate);
        when(rateMapper.toResponseDTO(any(Rate.class))).thenReturn(rateResponseDTO);

        RateResponseDTO result = rateService.update(1L, 1L, rateUpdateDTO);

        assertNotNull(result);
        verify(rateMapper, times(1)).updateEntityFromDto(any(RateUpdateDTO.class), any(Rate.class));
        verify(rateRepository, times(1)).save(any(Rate.class));
    }

    @Test
    void update_ShouldThrowException_WhenNotFound() {
        when(rateRepository.findByVisitorIdAndRestaurantId(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rateService.update(1L, 1L, rateUpdateDTO));
        verify(rateRepository, never()).save(any(Rate.class));
    }

    @Test
    void update_ShouldUpdateRestaurantRating_WhenRateUpdated() {
        when(rateRepository.findByVisitorIdAndRestaurantId(anyLong(), anyLong()))
                .thenReturn(Optional.of(rate));
        when(rateRepository.save(any(Rate.class))).thenReturn(rate);
        when(rateMapper.toResponseDTO(any(Rate.class))).thenReturn(rateResponseDTO);
        when(rateRepository.findAllByRestaurantId(anyLong())).thenReturn(List.of(rate));
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        rateService.update(1L, 1L, rateUpdateDTO);

        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void getRatesByRestaurant_ShouldReturnPagedRates() {
        Page<Rate> ratePage = new PageImpl<>(Collections.singletonList(rate));
        when(rateRepository.findAllByRestaurantId(anyLong(), any(Pageable.class)))
                .thenReturn(ratePage);
        when(rateMapper.toResponseDTO(any(Rate.class))).thenReturn(rateResponseDTO);

        Page<RateResponseDTO> result = rateService.getRatesByRestaurant(1L, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(rateRepository, times(1)).findAllByRestaurantId(anyLong(), any(Pageable.class));
    }
}