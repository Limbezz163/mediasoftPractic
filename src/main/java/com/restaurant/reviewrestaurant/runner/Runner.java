package com.restaurant.reviewrestaurant.runner;

import com.restaurant.reviewrestaurant.Services.RateService;
import com.restaurant.reviewrestaurant.Services.RestaurantService;
import com.restaurant.reviewrestaurant.Services.VisitorService;
import com.restaurant.reviewrestaurant.entity.Rate;
import com.restaurant.reviewrestaurant.entity.Restaurant;
import com.restaurant.reviewrestaurant.entity.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    private final RestaurantService restaurantService;
    private final VisitorService visitorService;
    private final RateService rateService;
    private final Restaurant sushiWok;
    private final Visitor ivan;
    @Autowired
    public Runner(RestaurantService restaurantService, VisitorService visitorService, RateService rateService, @Qualifier("testRestaurant1") Restaurant sushiWok, @Qualifier("testVisitor1") Visitor ivan) {
        this.restaurantService = restaurantService;
        this.visitorService = visitorService;
        this.rateService = rateService;
        this.sushiWok = sushiWok;
        this.ivan = ivan;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=== ТЕСТИРОВАНИЕ СЕРВИСОВ ===");


        System.out.println("\nВсе рестораны:");
        restaurantService.findAll().forEach(System.out::println);


        System.out.printf("\nИнформация о ресторане '%s': %s\n",
                sushiWok.getName(), restaurantService.findById(sushiWok.getId()));


        System.out.println("\nДобавляем новую оценку...");
        rateService.save(new Rate(ivan.getId(), sushiWok.getId(), 10, "Нормально"));
        Restaurant updated = restaurantService.findById(sushiWok.getId());
        System.out.printf("Обновленный рейтинг: %s\n", updated.getRating());
    }
}