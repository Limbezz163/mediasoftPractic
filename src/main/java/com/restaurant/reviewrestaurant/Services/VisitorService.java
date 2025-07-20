package com.restaurant.reviewrestaurant.Services;

import com.restaurant.reviewrestaurant.Repositories.VisitorRepository;
import com.restaurant.reviewrestaurant.dto.VisitorRequestDTO;
import com.restaurant.reviewrestaurant.dto.VisitorResponseDTO;
import com.restaurant.reviewrestaurant.entity.Visitor;
import com.restaurant.reviewrestaurant.mapper.VisitorMapper;
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
        visitorRepository.remove(id);
    }

    public List<VisitorResponseDTO> findAll() {
        return visitorRepository.findAll().stream()
                .map(visitorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public VisitorResponseDTO findById(Long id) {
        Visitor visitor = visitorRepository.findById(id);
        return visitorMapper.toResponseDTO(visitor);
    }

    public VisitorResponseDTO update(Long id, VisitorRequestDTO requestDTO) {
        Visitor visitor = visitorMapper.toEntity(requestDTO);
        visitor.setId(id);
        visitorRepository.update(id, visitor);
        return visitorMapper.toResponseDTO(visitorRepository.findById(id));
    }
}