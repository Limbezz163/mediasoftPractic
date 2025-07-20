package com.restaurant.reviewrestaurant.controller;

import com.restaurant.reviewrestaurant.dto.VisitorRequestDTO;
import com.restaurant.reviewrestaurant.dto.VisitorResponseDTO;
import com.restaurant.reviewrestaurant.Services.VisitorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
public class VisitorController {
    private final VisitorService visitorService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisitorResponseDTO createVisitor(@RequestBody @Valid VisitorRequestDTO requestDTO) {
        return visitorService.save(requestDTO);
    }


    @GetMapping
    public List<VisitorResponseDTO> getAllVisitors() {
        return visitorService.findAll();
    }

    @GetMapping("/{id}")
    public VisitorResponseDTO getVisitorById(@PathVariable Long id) {
        return visitorService.findById(id);
    }

    @PutMapping("/{id}")
    public VisitorResponseDTO updateVisitor(
            @PathVariable Long id,
            @RequestBody @Valid VisitorRequestDTO requestDTO
    ) {
        return visitorService.update(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVisitor(@PathVariable Long id) {
        visitorService.remove(id);
    }
}