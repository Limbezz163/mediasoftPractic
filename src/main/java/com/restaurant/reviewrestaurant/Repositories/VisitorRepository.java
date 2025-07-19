package com.restaurant.reviewrestaurant.Repositories;

import com.restaurant.reviewrestaurant.entity.Visitor;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VisitorRepository {
    private final List<Visitor> visitors= new ArrayList<>();

    public void save(Visitor visitor) {visitors.add(visitor);}

    public List<Visitor> findAll() {return visitors;}

    public void remove(Visitor visitor) {visitors.remove(visitor);}
}
