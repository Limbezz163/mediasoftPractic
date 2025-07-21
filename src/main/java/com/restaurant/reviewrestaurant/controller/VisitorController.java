package com.restaurant.reviewrestaurant.controller;

import com.restaurant.reviewrestaurant.dto.VisitorRequestDTO;
import com.restaurant.reviewrestaurant.dto.VisitorResponseDTO;
import com.restaurant.reviewrestaurant.Services.VisitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
@Tag(name = "Visitor Management", description = "API для управления посетителями ресторана")
public class VisitorController {
    private final VisitorService visitorService;

    @Operation(summary = "Создать нового посетителя", description = "Создает запись о новом посетителе ресторана")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Посетитель успешно создан",
                    content = @Content(schema = @Schema(implementation = VisitorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisitorResponseDTO createVisitor(@RequestBody @Valid VisitorRequestDTO requestDTO) {
        return visitorService.save(requestDTO);
    }

    @Operation(summary = "Получить всех посетителей", description = "Возвращает список всех посетителей ресторана")
    @ApiResponse(responseCode = "200", description = "Список посетителей успешно получен",
            content = @Content(schema = @Schema(implementation = VisitorResponseDTO.class)))
    @GetMapping
    public List<VisitorResponseDTO> getAllVisitors() {
        return visitorService.findAll();
    }

    @Operation(summary = "Получить посетителя по ID", description = "Возвращает информацию о конкретном посетителе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Посетитель найден",
                    content = @Content(schema = @Schema(implementation = VisitorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Посетитель не найден")
    })
    @GetMapping("/{id}")
    public VisitorResponseDTO getVisitorById(
            @Parameter(description = "ID посетителя", required = true, example = "1")
            @PathVariable Long id) {
        return visitorService.findById(id);
    }

    @Operation(summary = "Обновить информацию о посетителе", description = "Обновляет данные существующего посетителя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о посетителе успешно обновлена",
                    content = @Content(schema = @Schema(implementation = VisitorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
            @ApiResponse(responseCode = "404", description = "Посетитель не найден")
    })
    @PutMapping("/{id}")
    public VisitorResponseDTO updateVisitor(
            @Parameter(description = "ID посетителя", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody @Valid VisitorRequestDTO requestDTO) {
        return visitorService.update(id, requestDTO);
    }

    @Operation(summary = "Удалить посетителя", description = "Удаляет запись о посетителе по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Посетитель успешно удален"),
            @ApiResponse(responseCode = "404", description = "Посетитель не найден")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVisitor(
            @Parameter(description = "ID посетителя", required = true, example = "1")
            @PathVariable Long id) {
        visitorService.remove(id);
    }
}