package com.restaurant.reviewrestaurant.Services;

import com.restaurant.reviewrestaurant.Repositories.VisitorRepository;
import com.restaurant.reviewrestaurant.dto.VisitorRequestDTO;
import com.restaurant.reviewrestaurant.dto.VisitorResponseDTO;
import com.restaurant.reviewrestaurant.entity.Visitor;
import com.restaurant.reviewrestaurant.mapper.VisitorMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepository visitorRepository;
    private final VisitorMapper visitorMapper;

    public VisitorResponseDTO save(VisitorRequestDTO requestDTO) {
        Visitor visitor = visitorMapper.toEntity(requestDTO);
        Visitor saved = visitorRepository.save(visitor);
        return visitorMapper.toResponseDTO(saved);
    }

    public void remove(Long id) {
        if (!visitorRepository.existsById(id)) {
            throw new EntityNotFoundException("Посетитель не найден");
        }
        visitorRepository.deleteById(id);
    }

    public List<VisitorResponseDTO> findAll() {
        return visitorRepository.findAll().stream()
                .map(visitorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public VisitorResponseDTO findById(Long id) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Посетитель не найден"));
        return visitorMapper.toResponseDTO(visitor);
    }

    public VisitorResponseDTO update(Long id, VisitorRequestDTO requestDTO) {
        Visitor existing = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Посетитель не найден"));

        visitorMapper.updateEntityFromDto(requestDTO, existing);
        Visitor updated = visitorRepository.save(existing);
        return visitorMapper.toResponseDTO(updated);
    }
}