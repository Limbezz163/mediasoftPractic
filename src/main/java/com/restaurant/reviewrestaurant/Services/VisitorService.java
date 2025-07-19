package com.restaurant.reviewrestaurant.Services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.restaurant.reviewrestaurant.Repositories.VisitorRepository;
import com.restaurant.reviewrestaurant.entity.Visitor;

import java.util.List;
@RequiredArgsConstructor
@Service
public class VisitorService {
    private final VisitorRepository visitorRepository;

    public void save(Visitor visitor) {
        if(visitorRepository.findAll().contains(visitor)) {
            System.out.println("Такой посетитель уже есть");
        }
        else {visitorRepository.save(visitor);}

    }
    public void remove(Visitor visitor) {
        if (!visitorRepository.findAll().contains(visitor)) {
            throw new IllegalArgumentException("Посетитель не найден");
        }
        visitorRepository.remove(visitor);
    }

    public List<Visitor> findAll() {
        return visitorRepository.findAll();
    }

    public Visitor findById(Long id) {
        return visitorRepository.findAll().stream()
                .filter(visitor -> visitor.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Не найден посетитель с id  " + id));
    }
}