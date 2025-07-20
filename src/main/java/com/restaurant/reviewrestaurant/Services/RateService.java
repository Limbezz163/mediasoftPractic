package com.restaurant.reviewrestaurant.Services;

import com.restaurant.reviewrestaurant.dto.RateRequestDTO;
import com.restaurant.reviewrestaurant.dto.RateResponseDTO;
import com.restaurant.reviewrestaurant.dto.RateUpdateDTO;
import com.restaurant.reviewrestaurant.entity.Rate;
import com.restaurant.reviewrestaurant.mapper.RateMapper;
import com.restaurant.reviewrestaurant.Repositories.RateRepository;
import com.restaurant.reviewrestaurant.Repositories.RestaurantRepository;
import com.restaurant.reviewrestaurant.Repositories.VisitorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RateService {
    private final RateRepository rateRepository;
    private final RestaurantRepository restaurantRepository;
    private final VisitorRepository visitorRepository;
    private final RateMapper rateMapper;

    public RateResponseDTO save(RateRequestDTO requestDTO) {
        if (restaurantRepository.findById(requestDTO.getRestaurantId()) == null) {
            throw new IllegalArgumentException("Ресторан не найден");
        }

        if (visitorRepository.findById(requestDTO.getVisitorId()) == null) {
            throw new IllegalArgumentException("Посетитель не найден");
        }

        Rate rate = rateMapper.toEntity(requestDTO);
        rateRepository.save(rate);
        updateRestaurantRating(rate.getRestaurantId());
        return rateMapper.toResponseDTO(rate);
    }

    public void remove(Long visitorId, Long restaurantId) {
        rateRepository.remove(restaurantId, visitorId);
        updateRestaurantRating(restaurantId);
    }

    public List<RateResponseDTO> findAll() {
        return rateRepository.findAll().stream()
                .map(rateMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public RateResponseDTO findRateById(Long visitorId, Long restaurantId) {
        Rate rate = rateRepository.findById(visitorId, restaurantId);
        if (rate == null) {
            throw new EntityNotFoundException("Оценка не найдена");
        }
        return rateMapper.toResponseDTO(rate);
    }

    public RateResponseDTO update(Long visitorId, Long restaurantId, RateUpdateDTO updateDTO) {
        Rate existing = rateRepository.findById(visitorId, restaurantId);
        if (existing == null) {
            throw new EntityNotFoundException("Оценка не найдена");
        }

        rateMapper.updateEntityFromDto(updateDTO, existing);
        rateRepository.update(restaurantId, visitorId, existing);
        updateRestaurantRating(restaurantId);
        return rateMapper.toResponseDTO(existing);
    }

    private void updateRestaurantRating(Long restaurantId) {
        List<Rate> restaurantRates = rateRepository.findAll()
                .stream()
                .filter(rate -> rate.getRestaurantId().equals(restaurantId))
                .toList();

        if (restaurantRates.isEmpty()) {
            return;
        }

        BigDecimal newRating = BigDecimal.valueOf(
                restaurantRates.stream()
                        .mapToInt(Rate::getRating)
                        .average()
                        .orElse(0.0)
        ).setScale(2, RoundingMode.HALF_UP);

        restaurantRepository.findAll().stream()
                .filter(r -> r.getId().equals(restaurantId))
                .findFirst()
                .ifPresent(existingRestaurant -> {
                    existingRestaurant.setRating(newRating);
                });
    }
}