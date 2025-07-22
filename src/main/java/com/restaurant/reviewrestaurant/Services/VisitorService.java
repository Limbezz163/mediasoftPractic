package com.restaurant.reviewrestaurant.Services;

import com.restaurant.reviewrestaurant.Repositories.VisitorRepository;
import com.restaurant.reviewrestaurant.dto.VisitorRequestDTO;
import com.restaurant.reviewrestaurant.dto.VisitorResponseDTO;
import com.restaurant.reviewrestaurant.entity.Visitor;
import com.restaurant.reviewrestaurant.exception.ResourceNotFoundException;
import com.restaurant.reviewrestaurant.mapper.VisitorMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VisitorService {
    private final VisitorRepository visitorRepository;
    private final VisitorMapper visitorMapper;

    @Transactional(readOnly = true)
    public List<VisitorResponseDTO> findAll() {
        log.info("Получение списка всех посетителей");
        return visitorRepository.findAll().stream()
                .map(visitorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VisitorResponseDTO findById(Long id) {
        log.info("Поиск посетителя с id: {}", id);
        return visitorRepository.findById(id)
                .map(visitorMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Посетитель с id " + id + " не найден"));
    }

    public VisitorResponseDTO save(VisitorRequestDTO requestDTO) {
        log.info("Создание нового посетителя с именем: {}", requestDTO.getName());
        Visitor visitor = visitorMapper.toEntity(requestDTO);
        Visitor savedVisitor = visitorRepository.save(visitor);
        log.debug("Создан посетитель с id: {}", savedVisitor.getId());
        return visitorMapper.toResponseDTO(savedVisitor);
    }

    public VisitorResponseDTO update(Long id, VisitorRequestDTO requestDTO) {
        log.info("Обновление посетителя с id: {}", id);
        return visitorRepository.findById(id)
                .map(existingVisitor -> {
                    visitorMapper.updateEntityFromDto(requestDTO, existingVisitor);
                    Visitor updatedVisitor = visitorRepository.save(existingVisitor);
                    log.debug("Обновлен посетитель с id: {}", id);
                    return visitorMapper.toResponseDTO(updatedVisitor);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Посетитель с id " + id + " не найден"));
    }

    public void remove(Long id) {
        log.info("Удаление посетителя с id: {}", id);
        try {
            visitorRepository.deleteById(id);
            log.debug("Посетитель с id {} удален", id);
        } catch (EmptyResultDataAccessException ex) {
            log.warn("Попытка удалить несуществующего посетителя с id: {}", id);
            throw new ResourceNotFoundException("Посетитель с id " + id + " не найден");
        }
    }
}