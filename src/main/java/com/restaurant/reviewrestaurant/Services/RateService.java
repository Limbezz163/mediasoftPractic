package com.restaurant.reviewrestaurant.Services;

import com.restaurant.reviewrestaurant.dto.RateRequestDTO;
import com.restaurant.reviewrestaurant.dto.RateResponseDTO;
import com.restaurant.reviewrestaurant.dto.RateUpdateDTO;
import com.restaurant.reviewrestaurant.entity.Rate;
import com.restaurant.reviewrestaurant.entity.Restaurant;
import com.restaurant.reviewrestaurant.entity.Visitor;
import com.restaurant.reviewrestaurant.mapper.RateMapper;
import com.restaurant.reviewrestaurant.Repositories.RateRepository;
import com.restaurant.reviewrestaurant.Repositories.RestaurantRepository;
import com.restaurant.reviewrestaurant.Repositories.VisitorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RateService {
    private final RateRepository rateRepository;
    private final RestaurantRepository restaurantRepository;
    private final VisitorRepository visitorRepository;
    private final RateMapper rateMapper;

    public RateResponseDTO save(RateRequestDTO requestDTO) {
        if (!restaurantRepository.existsById(requestDTO.getRestaurantId())) {
            throw new IllegalArgumentException("Ресторан не найден");
        }

        if (!visitorRepository.existsById(requestDTO.getVisitorId())) {
            throw new IllegalArgumentException("Посетитель не найден");
        }

        if (rateRepository.existsByVisitorIdAndRestaurantId(
                requestDTO.getVisitorId(),
                requestDTO.getRestaurantId())) {
            throw new IllegalArgumentException(
                    "Отзыв от этого пользователя для данного ресторана уже существует");
        }

        Rate rate = rateMapper.toEntity(requestDTO);
        Rate saved = rateRepository.save(rate);
        updateRestaurantRating(requestDTO.getRestaurantId());
        return rateMapper.toResponseDTO(saved);
    }

    public void remove(Long visitorId, Long restaurantId) {
        if (!rateRepository.existsByVisitorIdAndRestaurantId(visitorId, restaurantId)) {
            throw new EntityNotFoundException("Оценка не найдена");
        }
        rateRepository.deleteByVisitorIdAndRestaurantId(visitorId, restaurantId);
        updateRestaurantRating(restaurantId);
    }

    public List<RateResponseDTO> findAll() {
        return rateRepository.findAll().stream()
                .map(rateMapper::toResponseDTO)
                .toList();
    }

    public RateResponseDTO findRateById(Long visitorId, Long restaurantId) {
        Rate rate = rateRepository.findByVisitorIdAndRestaurantId(visitorId, restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Оценка не найдена"));
        return rateMapper.toResponseDTO(rate);
    }

    public RateResponseDTO update(Long visitorId, Long restaurantId, RateUpdateDTO updateDTO) {
        Rate existing = rateRepository.findByVisitorIdAndRestaurantId(visitorId, restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Отзыв не найден"));

        rateMapper.updateEntityFromDto(updateDTO, existing);
        Rate updated = rateRepository.save(existing);
        updateRestaurantRating(restaurantId);
        return rateMapper.toResponseDTO(updated);
    }

    public Page<RateResponseDTO> getRatesByRestaurant(Long restaurantId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("rating").descending());
        return rateRepository.findAllByRestaurantId(restaurantId, pageable)
                .map(rateMapper::toResponseDTO);
    }

    private void updateRestaurantRating(Long restaurantId) {
        List<Rate> rates = rateRepository.findAllByRestaurantId(restaurantId);
        if (rates.isEmpty()) return;

        double average = rates.stream()
                .mapToInt(Rate::getRating)
                .average()
                .orElse(0.0);

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Ресторан не найден"));
        restaurant.setRating(BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP));
        restaurantRepository.save(restaurant);
    }
}