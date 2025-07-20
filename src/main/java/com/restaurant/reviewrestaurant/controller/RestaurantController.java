package com.restaurant.reviewrestaurant.controller;

import com.restaurant.reviewrestaurant.dto.RestaurantRequestDTO;
import com.restaurant.reviewrestaurant.dto.RestaurantResponseDTO;
import com.restaurant.reviewrestaurant.Services.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponseDTO create(@RequestBody @Valid RestaurantRequestDTO requestDTO) {
        return restaurantService.save(requestDTO);
    }

    @GetMapping
    public List<RestaurantResponseDTO> getAll() {
        return restaurantService.findAll();
    }

    @GetMapping("/{id}")
    public RestaurantResponseDTO getById(@PathVariable Long id) {
        return restaurantService.findById(id);
    }

    @PutMapping("/{id}")
    public RestaurantResponseDTO update(
            @PathVariable Long id,
            @RequestBody @Valid RestaurantRequestDTO requestDTO
    ) {
        return restaurantService.update(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        restaurantService.remove(id);
    }
}