package com.restaurant.reviewrestaurant.controller;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.reviewrestaurant.dto.VisitorRequestDTO;
import com.restaurant.reviewrestaurant.dto.VisitorResponseDTO;
import com.restaurant.reviewrestaurant.Services.VisitorService;
import com.restaurant.reviewrestaurant.enums.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VisitorController.class)
public class VisitorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VisitorService visitorService;

    private VisitorRequestDTO createValidRequest() {
        return VisitorRequestDTO.builder()
                .name("John Doe")
                .age(25)
                .gender(Gender.MALE)
                .build();
    }

    private VisitorResponseDTO createResponseDTO() {
        return VisitorResponseDTO.builder()
                .id(1L)
                .name("John Doe")
                .age(25)
                .gender(Gender.MALE)
                .build();
    }

    @Test
    void createVisitor_ShouldReturnCreated() throws Exception {
        VisitorResponseDTO response = createResponseDTO();
        when(visitorService.save(any())).thenReturn(response);

        mockMvc.perform(post("/api/visitors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createValidRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getAllVisitors_ShouldReturnList() throws Exception {
        List<VisitorResponseDTO> visitors = Arrays.asList(
                createResponseDTO(),
                VisitorResponseDTO.builder()
                        .id(2L)
                        .name("Jane Doe")
                        .age(30)
                        .gender(Gender.FEMALE)
                        .build()
        );

        when(visitorService.findAll()).thenReturn(visitors);

        mockMvc.perform(get("/api/visitors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getVisitorById_ShouldReturnVisitor() throws Exception {
        when(visitorService.findById(1L)).thenReturn(createResponseDTO());

        mockMvc.perform(get("/api/visitors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void updateVisitor_ShouldReturnUpdated() throws Exception {
        VisitorResponseDTO updated = VisitorResponseDTO.builder()
                .id(1L)
                .name("Updated Name")
                .age(26)
                .gender(Gender.MALE)
                .build();

        when(visitorService.update(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(put("/api/visitors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createValidRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void deleteVisitor_ShouldReturnNoContent() throws Exception {
        doNothing().when(visitorService).remove(1L);

        mockMvc.perform(delete("/api/visitors/1"))
                .andExpect(status().isNoContent());
    }
}