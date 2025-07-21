package com.restaurant.reviewrestaurant.controller;

import com.restaurant.reviewrestaurant.dto.RateRequestDTO;
import com.restaurant.reviewrestaurant.dto.RateResponseDTO;
import com.restaurant.reviewrestaurant.dto.RateUpdateDTO;
import com.restaurant.reviewrestaurant.Services.RateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
            summary = "Добавить новую оценку",
            description = "Создает новую оценку ресторана от посетителя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Оценка успешно создана",
                    content = @Content(schema = @Schema(implementation = RateResponseDTO.class)))
                    })
    public RateResponseDTO create(
            @RequestBody @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания оценки",
                    content = @Content(
                            schema = @Schema(implementation = RateRequestDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"visitorId\": 1, \"restaurantId\": 1, \"rating\": 5, \"reviewText\": \"Отличный ресторан!\"}"
                            )
                    )
            )
            RateRequestDTO requestDTO) {
        return rateService.save(requestDTO);
    }

    @GetMapping
    @Operation(
            summary = "Получить все оценки",
            description = "Возвращает список всех оценок всех ресторанов"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список оценок успешно получен",
            content = @Content(schema = @Schema(implementation = RateResponseDTO[].class))
    )
    public List<RateResponseDTO> getAll() {
        return rateService.findAll();
    }

    @GetMapping("/{visitorId}/{restaurantId}")
    @Operation(
            summary = "Получить оценку по ID посетителя и ресторана",
            description = "Возвращает оценку конкретного ресторана от конкретного посетителя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Оценка успешно найдена",
                    content = @Content(schema = @Schema(implementation = RateResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Оценка не найдена",
                    content = @Content
            )
    })
    public RateResponseDTO getById(
            @Parameter(description = "ID посетителя", example = "1")
            @PathVariable Long visitorId,

            @Parameter(description = "ID ресторана", example = "1")
            @PathVariable Long restaurantId) {
        return rateService.findRateById(visitorId, restaurantId);
    }

    @PutMapping("/{visitorId}/{restaurantId}")
    @Operation(
            summary = "Обновить оценку",
            description = "Обновляет оценку ресторана от посетителя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Оценка успешно обновлена",
                    content = @Content(schema = @Schema(implementation = RateResponseDTO.class)))
                    })
    public RateResponseDTO update(
            @Parameter(description = "ID посетителя", example = "1")
            @PathVariable Long visitorId,

            @Parameter(description = "ID ресторана", example = "1")
            @PathVariable Long restaurantId,

            @RequestBody @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления оценки",
                    content = @Content(
                            schema = @Schema(implementation = RateUpdateDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"rating\": 4, \"reviewText\": \"Хороший ресторан, но можно улучшить сервис\"}"
                            )
                    )
            )
            RateUpdateDTO updateDTO) {
        return rateService.update(visitorId, restaurantId, updateDTO);
    }

    @DeleteMapping("/{visitorId}/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить оценку",
            description = "Удаляет оценку ресторана от посетителя"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Оценка успешно удалена"
    )
    public void delete(
            @Parameter(description = "ID посетителя", example = "1")
            @PathVariable Long visitorId,

            @Parameter(description = "ID ресторана", example = "1")
            @PathVariable Long restaurantId) {
        rateService.remove(visitorId, restaurantId);
    }
}