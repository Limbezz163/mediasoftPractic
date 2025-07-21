package com.restaurant.reviewrestaurant.Services;
import com.restaurant.reviewrestaurant.dto.VisitorRequestDTO;
import com.restaurant.reviewrestaurant.dto.VisitorResponseDTO;
import com.restaurant.reviewrestaurant.entity.Visitor;
import com.restaurant.reviewrestaurant.enums.Gender;
import com.restaurant.reviewrestaurant.mapper.VisitorMapper;
import com.restaurant.reviewrestaurant.Repositories.VisitorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
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

    @Test
    void save_ShouldReturnSavedVisitorResponseDTO() {
        
        VisitorRequestDTO requestDTO = VisitorRequestDTO.builder()
                .name("John Doe")
                .age(25)
                .gender(Gender.MALE)
                .build();

        Visitor visitor = Visitor.builder()
                .name("John Doe")
                .age(25)
                .gender(Gender.MALE)
                .build();

        Visitor savedVisitor = Visitor.builder()
                .id(1L)
                .name("John Doe")
                .age(25)
                .gender(Gender.MALE)
                .build();

        VisitorResponseDTO expectedResponse = VisitorResponseDTO.builder()
                .id(1L)
                .name("John Doe")
                .age(25)
                .gender(Gender.MALE)
                .build();

        when(visitorMapper.toEntity(requestDTO)).thenReturn(visitor);
        when(visitorRepository.save(visitor)).thenReturn(savedVisitor);
        when(visitorMapper.toResponseDTO(savedVisitor)).thenReturn(expectedResponse);

        
        VisitorResponseDTO result = visitorService.save(requestDTO);

        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals(25, result.getAge());
        assertEquals(Gender.MALE, result.getGender());

        verify(visitorMapper).toEntity(requestDTO);
        verify(visitorRepository).save(visitor);
        verify(visitorMapper).toResponseDTO(savedVisitor);
    }

    @Test
    void remove_WhenVisitorExists_ShouldDeleteVisitor() {
        
        Long visitorId = 1L;
        when(visitorRepository.existsById(visitorId)).thenReturn(true);

        
        visitorService.remove(visitorId);

        
        verify(visitorRepository).deleteById(visitorId);
    }

    @Test
    void remove_WhenVisitorNotExists_ShouldThrowException() {
        
        Long visitorId = 1L;
        when(visitorRepository.existsById(visitorId)).thenReturn(false);

        
        assertThrows(EntityNotFoundException.class, () -> visitorService.remove(visitorId));
        verify(visitorRepository, never()).deleteById(visitorId);
    }

    @Test
    void findAll_ShouldReturnListOfVisitorResponseDTO() {
        
        Visitor visitor1 = Visitor.builder()
                .id(1L)
                .name("John Doe")
                .age(25)
                .gender(Gender.MALE)
                .build();

        Visitor visitor2 = Visitor.builder()
                .id(2L)
                .name("Jane Smith")
                .age(30)
                .gender(Gender.FEMALE)
                .build();

        VisitorResponseDTO response1 = VisitorResponseDTO.builder()
                .id(1L)
                .name("John Doe")
                .age(25)
                .gender(Gender.MALE)
                .build();

        VisitorResponseDTO response2 = VisitorResponseDTO.builder()
                .id(2L)
                .name("Jane Smith")
                .age(30)
                .gender(Gender.FEMALE)
                .build();

        when(visitorRepository.findAll()).thenReturn(List.of(visitor1, visitor2));
        when(visitorMapper.toResponseDTO(visitor1)).thenReturn(response1);
        when(visitorMapper.toResponseDTO(visitor2)).thenReturn(response2);

        
        List<VisitorResponseDTO> result = visitorService.findAll();

        
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Jane Smith", result.get(1).getName());
    }

    @Test
    void findById_WhenVisitorExists_ShouldReturnVisitorResponseDTO() {
        
        Long visitorId = 1L;
        Visitor visitor = Visitor.builder()
                .id(visitorId)
                .name("John Doe")
                .age(25)
                .gender(Gender.MALE)
                .build();

        VisitorResponseDTO expectedResponse = VisitorResponseDTO.builder()
                .id(visitorId)
                .name("John Doe")
                .age(25)
                .gender(Gender.MALE)
                .build();

        when(visitorRepository.findById(visitorId)).thenReturn(Optional.of(visitor));
        when(visitorMapper.toResponseDTO(visitor)).thenReturn(expectedResponse);

        
        VisitorResponseDTO result = visitorService.findById(visitorId);

        
        assertNotNull(result);
        assertEquals(visitorId, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals(25, result.getAge());
        assertEquals(Gender.MALE, result.getGender());
    }

    @Test
    void findById_WhenVisitorNotExists_ShouldThrowException() {
        
        Long visitorId = 1L;
        when(visitorRepository.findById(visitorId)).thenReturn(Optional.empty());

        
        assertThrows(EntityNotFoundException.class, () -> visitorService.findById(visitorId));
    }

    @Test
    void update_WhenVisitorExists_ShouldReturnUpdatedVisitorResponseDTO() {
        
        Long visitorId = 1L;
        VisitorRequestDTO requestDTO = VisitorRequestDTO.builder()
                .name("Updated Name")
                .age(30)
                .gender(Gender.FEMALE)
                .build();

        Visitor existingVisitor = Visitor.builder()
                .id(visitorId)
                .name("Original Name")
                .age(25)
                .gender(Gender.MALE)
                .build();

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
        doAnswer(invocation -> {
            VisitorRequestDTO dto = invocation.getArgument(0);
            Visitor entity = invocation.getArgument(1);
            entity.setName(dto.getName());
            entity.setAge(dto.getAge());
            entity.setGender(dto.getGender());
            return null;
        }).when(visitorMapper).updateEntityFromDto(requestDTO, existingVisitor);
        when(visitorRepository.save(existingVisitor)).thenReturn(updatedVisitor);
        when(visitorMapper.toResponseDTO(updatedVisitor)).thenReturn(expectedResponse);

        
        VisitorResponseDTO result = visitorService.update(visitorId, requestDTO);

        
        assertNotNull(result);
        assertEquals(visitorId, result.getId());
        assertEquals("Updated Name", result.getName());
        assertEquals(30, result.getAge());
        assertEquals(Gender.FEMALE, result.getGender());

        verify(visitorRepository).findById(visitorId);
        verify(visitorMapper).updateEntityFromDto(requestDTO, existingVisitor);
        verify(visitorRepository).save(existingVisitor);
        verify(visitorMapper).toResponseDTO(updatedVisitor);
    }


    @Test
    void update_WhenVisitorNotExists_ShouldThrowException() {
        
        Long visitorId = 1L;
        VisitorRequestDTO requestDTO = VisitorRequestDTO.builder()
                .name("Updated Name")
                .age(30)
                .gender(Gender.FEMALE)
                .build();

        when(visitorRepository.findById(visitorId)).thenReturn(Optional.empty());

        
        assertThrows(EntityNotFoundException.class, () -> visitorService.update(visitorId, requestDTO));
        verify(visitorRepository, never()).save(any());
    }
}