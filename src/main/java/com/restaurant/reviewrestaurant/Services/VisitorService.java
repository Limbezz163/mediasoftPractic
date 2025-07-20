package com.restaurant.reviewrestaurant.Services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.restaurant.reviewrestaurant.Repositories.VisitorRepository;
import com.restaurant.reviewrestaurant.entity.Visitor;

import java.util.List;
@Service
@RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepository visitorRepository;
    private long VISITOR_ID = 0;

    public void save(Visitor visitor) {
        visitor.setId(++VISITOR_ID);
        visitorRepository.save(visitor);
    }

    public void remove(Visitor visitor) {
        if (visitorRepository.findById(visitor.getId()) == null) {
            throw new EntityNotFoundException("Посетитель не найден");
        }
        visitorRepository.remove(visitor.getId());
    }

    public List<Visitor> findAll() {
        return visitorRepository.findAll();
    }

    public Visitor findById(Long id) {
        return visitorRepository.findById(id);
    }

    public void update(Long id, Visitor updatedVisitor) {
        Visitor existingVisitor = visitorRepository.findById(id);
        if (existingVisitor == null) {
            throw new EntityNotFoundException("Посетитель с ID " + id + " не найден");
        }
        visitorRepository.update(id, updatedVisitor);
        }
}