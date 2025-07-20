package com.restaurant.reviewrestaurant.controller;

import com.restaurant.reviewrestaurant.dto.RateRequestDTO;
import com.restaurant.reviewrestaurant.dto.RateResponseDTO;
import com.restaurant.reviewrestaurant.dto.RateUpdateDTO;
import com.restaurant.reviewrestaurant.Services.RateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rates")
@RequiredArgsConstructor
@Tag(name = "Управление оценками", description = "API для работы с оценками ресторанов")
public class RateController {
    private final RateService rateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавить новую оценку")
    public RateResponseDTO create(@RequestBody @Valid RateRequestDTO requestDTO) {
        return rateService.save(requestDTO);
    }

    @GetMapping
    @Operation(summary = "Получить все оценки")
    public List<RateResponseDTO> getAll() {
        return rateService.findAll();
    }

    @GetMapping("/{visitorId}/{restaurantId}")
    @Operation(summary = "Получить оценку по ID посетителя и ресторана")
    public RateResponseDTO getById(
            @PathVariable Long visitorId,
            @PathVariable Long restaurantId) {
        return rateService.findRateById(visitorId, restaurantId);
    }

    @PutMapping("/{visitorId}/{restaurantId}")
    @Operation(summary = "Обновить оценку")
    public RateResponseDTO update(
            @PathVariable Long visitorId,
            @PathVariable Long restaurantId,
            @RequestBody @Valid RateUpdateDTO updateDTO) {
        return rateService.update(visitorId, restaurantId, updateDTO);
    }

    @DeleteMapping("/{visitorId}/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить оценку")
    public void delete(
            @PathVariable Long visitorId,
            @PathVariable Long restaurantId) {
        rateService.remove(visitorId, restaurantId);
    }
}