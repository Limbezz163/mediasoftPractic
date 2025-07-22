package com.restaurant.reviewrestaurant.Services;

import com.restaurant.reviewrestaurant.dto.VisitorRequestDTO;
import com.restaurant.reviewrestaurant.dto.VisitorResponseDTO;
import com.restaurant.reviewrestaurant.entity.Visitor;
import com.restaurant.reviewrestaurant.enums.Gender;
import com.restaurant.reviewrestaurant.exception.ResourceNotFoundException;
import com.restaurant.reviewrestaurant.mapper.VisitorMapper;
import com.restaurant.reviewrestaurant.Repositories.VisitorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitorServiceTest {

    @Mock
    private VisitorRepository visitorRepository;

    @Mock
    private VisitorMapper visitorMapper;

    @InjectMocks
    private VisitorService visitorService;

    private VisitorRequestDTO createTestRequestDTO() {
        return VisitorRequestDTO.builder()
                .name("John Doe")
                .age(25)
                .gender(Gender.MALE)
                .build();
    }

    private Visitor createTestVisitor(Long id) {
        return Visitor.builder()
                .id(id)
                .name("John Doe")
                .age(25)
                .gender(Gender.MALE)
                .build();
    }

    private VisitorResponseDTO createTestResponseDTO(Long id) {
        return VisitorResponseDTO.builder()
                .id(id)
                .name("John Doe")
                .age(25)
                .gender(Gender.MALE)
                .build();
    }

    @Test
    void save_ShouldReturnSavedVisitorResponseDTO() {
        VisitorRequestDTO requestDTO = createTestRequestDTO();
        Visitor visitor = createTestVisitor(null);
        Visitor savedVisitor = createTestVisitor(1L);
        VisitorResponseDTO expectedResponse = createTestResponseDTO(1L);

        when(visitorMapper.toEntity(requestDTO)).thenReturn(visitor);
        when(visitorRepository.save(visitor)).thenReturn(savedVisitor);
        when(visitorMapper.toResponseDTO(savedVisitor)).thenReturn(expectedResponse);

        VisitorResponseDTO result = visitorService.save(requestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(visitorMapper).toEntity(requestDTO);
        verify(visitorRepository).save(visitor);
        verify(visitorMapper).toResponseDTO(savedVisitor);
    }

    @Test
    void remove_ShouldDeleteVisitor() {
        Long visitorId = 1L;
        doNothing().when(visitorRepository).deleteById(visitorId);

        visitorService.remove(visitorId);

        verify(visitorRepository).deleteById(visitorId);
    }

    @Test
    void remove_WhenVisitorNotExists_ShouldThrowException() {
        Long visitorId = 1L;
        doThrow(ResourceNotFoundException.class).when(visitorRepository).deleteById(visitorId);

        assertThrows(ResourceNotFoundException.class, () -> visitorService.remove(visitorId));
    }

    @Test
    void findAll_ShouldReturnListOfVisitorResponseDTO() {
        Visitor visitor1 = createTestVisitor(1L);
        Visitor visitor2 = createTestVisitor(2L);
        VisitorResponseDTO response1 = createTestResponseDTO(1L);
        VisitorResponseDTO response2 = createTestResponseDTO(2L);

        when(visitorRepository.findAll()).thenReturn(List.of(visitor1, visitor2));
        when(visitorMapper.toResponseDTO(visitor1)).thenReturn(response1);
        when(visitorMapper.toResponseDTO(visitor2)).thenReturn(response2);

        List<VisitorResponseDTO> result = visitorService.findAll();

        assertEquals(2, result.size());
        verify(visitorRepository).findAll();
        verify(visitorMapper).toResponseDTO(visitor1);
        verify(visitorMapper).toResponseDTO(visitor2);
    }

    @Test
    void findAll_WhenNoVisitors_ShouldReturnEmptyList() {
        when(visitorRepository.findAll()).thenReturn(Collections.emptyList());

        List<VisitorResponseDTO> result = visitorService.findAll();

        assertTrue(result.isEmpty());
        verify(visitorRepository).findAll();
    }

    @Test
    void findById_WhenVisitorExists_ShouldReturnVisitorResponseDTO() {
        Long visitorId = 1L;
        Visitor visitor = createTestVisitor(visitorId);
        VisitorResponseDTO expectedResponse = createTestResponseDTO(visitorId);

        when(visitorRepository.findById(visitorId)).thenReturn(Optional.of(visitor));
        when(visitorMapper.toResponseDTO(visitor)).thenReturn(expectedResponse);

        VisitorResponseDTO result = visitorService.findById(visitorId);

        assertNotNull(result);
        assertEquals(visitorId, result.getId());
        verify(visitorRepository).findById(visitorId);
        verify(visitorMapper).toResponseDTO(visitor);
    }

    @Test
    void findById_WhenVisitorNotExists_ShouldThrowException() {
        Long visitorId = 1L;
        when(visitorRepository.findById(visitorId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> visitorService.findById(visitorId));
    }

    @Test
    void update_WhenVisitorExists_ShouldReturnUpdatedVisitorResponseDTO() {
        Long visitorId = 1L;
        VisitorRequestDTO requestDTO = VisitorRequestDTO.builder()
                .name("Updated Name")
                .age(30)
                .gender(Gender.FEMALE)
                .build();

        Visitor existingVisitor = createTestVisitor(visitorId);
        Visitor updatedVisitor = Visitor.builder()
                .id(visitorId)
                .name("Updated Name")
                .age(30)
                .gender(Gender.FEMALE)
                .build();

        VisitorResponseDTO expectedResponse = VisitorResponseDTO.builder()
                .id(visitorId)
                .name("Updated Name")
                .age(30)
                .gender(Gender.FEMALE)
                .build();

        when(visitorRepository.findById(visitorId)).thenReturn(Optional.of(existingVisitor));
        when(visitorRepository.save(existingVisitor)).thenReturn(updatedVisitor);
        when(visitorMapper.toResponseDTO(updatedVisitor)).thenReturn(expectedResponse);

        VisitorResponseDTO result = visitorService.update(visitorId, requestDTO);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        verify(visitorRepository).findById(visitorId);
        verify(visitorMapper).updateEntityFromDto(requestDTO, existingVisitor);
        verify(visitorRepository).save(existingVisitor);
    }

    @Test
    void update_WhenVisitorNotExists_ShouldThrowException() {
        Long visitorId = 1L;
        VisitorRequestDTO requestDTO = createTestRequestDTO();

        when(visitorRepository.findById(visitorId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> visitorService.update(visitorId, requestDTO));
    }
}