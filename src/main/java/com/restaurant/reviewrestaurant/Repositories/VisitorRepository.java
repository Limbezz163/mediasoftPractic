package com.restaurant.reviewrestaurant.Repositories;

import com.restaurant.reviewrestaurant.entity.Visitor;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VisitorRepository {
    private final List<Visitor> visitors= new ArrayList<>();

    public void save(Visitor visitor) {visitors.add(visitor);}

    public List<Visitor> findAll() {return visitors;}
    
    public Visitor findById(Long id) {
        return visitors.stream()
                .filter(restaurant -> restaurant.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Не найден посетитель с id " + id));
    }

    public void remove(long id) {visitors.removeIf(r -> r.getId().equals(id));;}
    
    public void update(long id, Visitor visitor) {
        Visitor oldVisitor = findById(id);
        oldVisitor.setName(visitor.getName());
        oldVisitor.setAge(visitor.getAge());
        oldVisitor.setGender(visitor.getGender());
    }
}
