package com.restaurant.reviewrestaurant.controller;

import com.restaurant.reviewrestaurant.dto.RestaurantRequestDTO;
import com.restaurant.reviewrestaurant.dto.RestaurantResponseDTO;
import com.restaurant.reviewrestaurant.Services.RestaurantService;
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
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@Tag(name = "Управление ресторанами", description = "API для работы с ресторанами")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создать новый ресторан",
            description = "Создает новый ресторан с указанными параметрами"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Ресторан успешно создан",
                    content = @Content(schema = @Schema(implementation = RestaurantResponseDTO.class)))
                    })
    public RestaurantResponseDTO create(
            @RequestBody @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания ресторана",
                    content = @Content(
                            schema = @Schema(implementation = RestaurantRequestDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "name": "Гастрономический рай",
                                        "description": "Ресторан высокой кухни",
                                        "cuisineType": "ITALIAN",
                                        "averagePrice": 1500.50,
                                        "rating": 4.5
                                    }
                                    """
                            )
                    )
            )
            RestaurantRequestDTO requestDTO) {
        return restaurantService.save(requestDTO);
    }

    @GetMapping
    @Operation(
            summary = "Получить все рестораны",
            description = "Возвращает список всех ресторанов"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список ресторанов успешно получен",
            content = @Content(schema = @Schema(implementation = RestaurantResponseDTO[].class))
    )
    public List<RestaurantResponseDTO> getAll() {
        return restaurantService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить ресторан по ID",
            description = "Возвращает информацию о конкретном ресторане"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ресторан успешно найден",
                    content = @Content(schema = @Schema(implementation = RestaurantResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ресторан не найден",
                    content = @Content
            )
    })
    public RestaurantResponseDTO getById(
            @Parameter(description = "ID ресторана", example = "1")
            @PathVariable Long id) {
        return restaurantService.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить ресторан",
            description = "Обновляет информацию о ресторане"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ресторан успешно обновлен",
                    content = @Content(schema = @Schema(implementation = RestaurantResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ресторан не найден",
                    content = @Content
            )
    })
    public RestaurantResponseDTO update(
            @Parameter(description = "ID ресторана", example = "1")
            @PathVariable Long id,

            @RequestBody @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления ресторана",
                    content = @Content(
                            schema = @Schema(implementation = RestaurantRequestDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "name": "Обновленный Гастрономический рай",
                                        "description": "Лучший ресторан высокой кухни",
                                        "cuisineType": "ITALIAN",
                                        "averagePrice": 1800.00,
                                        "rating": 4.8
                                    }
                                    """
                            )
                    )
            )
            RestaurantRequestDTO requestDTO) {
        return restaurantService.update(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить ресторан",
            description = "Удаляет ресторан по указанному ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Ресторан успешно удален"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ресторан не найден",
                    content = @Content
            )
    })
    public void delete(
            @Parameter(description = "ID ресторана", example = "1")
            @PathVariable Long id) {
        restaurantService.remove(id);
    }
}