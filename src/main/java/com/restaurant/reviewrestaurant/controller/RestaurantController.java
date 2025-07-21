package com.restaurant.reviewrestaurant.controller;
import com.restaurant.reviewrestaurant.dto.RestaurantResponseDTO;
import com.restaurant.reviewrestaurant.Services.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@Tag(name = "Управление ресторанами", description = "API для работы с ресторанами")
public class RestaurantController {
    private final RestaurantService restaurantService;

    // ... существующие методы (create, getAll, getById, update, delete)

    @GetMapping("/with-rating")
    @Operation(
            summary = "Найти рестораны с минимальным рейтингом",
            description = "Возвращает список ресторанов с рейтингом не ниже указанного"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список ресторанов успешно получен",
            content = @Content(schema = @Schema(implementation = RestaurantResponseDTO[].class))
    )
    public List<RestaurantResponseDTO> getRestaurantsWithMinRating(
            @Parameter(description = "Минимальный рейтинг", example = "4.0")
            @RequestParam BigDecimal minRating,
            @Parameter(description = "Использовать JPQL запрос", example = "false")
            @RequestParam(defaultValue = "false") boolean useJpql) {
        return useJpql
                ? restaurantService.getRestaurantsWithMinRatingJpql(minRating)
                : restaurantService.getRestaurantsWithMinRating(minRating);
    }
}