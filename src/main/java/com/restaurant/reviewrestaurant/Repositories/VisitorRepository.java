package com.restaurant.reviewrestaurant.Repositories;

import com.restaurant.reviewrestaurant.entity.Visitor;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class VisitorRepository {
    private final List<Visitor> visitors = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1); // Автоинкремент ID

    public Visitor save(Visitor visitor) {
        if (visitor.getId() == null) {
            visitor.setId(idCounter.getAndIncrement());
            visitors.add(visitor);
        } else {
            update(visitor.getId(), visitor);
        }
        return visitor;
    }

    public List<Visitor> findAll() {
        return new ArrayList<>(visitors); // Возвращаем копию
    }

    public Visitor findById(Long id) {
        return visitors.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Посетитель не найден"));
    }

    public void remove(Long id) {
        visitors.removeIf(v -> v.getId().equals(id));
    }

    public void update(Long id, Visitor visitor) {
        Visitor oldVisitor = findById(id);
        oldVisitor.setName(visitor.getName());
        oldVisitor.setAge(visitor.getAge());
        oldVisitor.setGender(visitor.getGender());
    }
}