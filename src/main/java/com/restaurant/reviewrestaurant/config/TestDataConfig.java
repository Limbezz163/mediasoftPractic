package com.restaurant.reviewrestaurant.config;

import com.restaurant.reviewrestaurant.entity.*;
import com.restaurant.reviewrestaurant.enums.CuisineType;
import com.restaurant.reviewrestaurant.enums.Gender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class TestDataConfig {

    @Bean
    public Restaurant testRestaurant1() {
        return new Restaurant(1L, "Суши Wok", "Японская кухня",
                CuisineType.JAPANESE, BigDecimal.valueOf(1500), BigDecimal.ZERO);
    }

    @Bean
    public Restaurant testRestaurant2() {
        return new Restaurant(2L, "Итальянский уголок", "Паста и пицца",
                CuisineType.RUSSIAN, BigDecimal.valueOf(2000), BigDecimal.ZERO);
    }

    @Bean
    public Visitor testVisitor1() {
        return new Visitor(1L, "Иван", 25, Gender.MALE);
    }

    @Bean
    public Visitor testVisitor2() {
        return new Visitor(2L, "Анна", 30, Gender.FEMALE);
    }

    @Bean
    public Rate testRate1() {
        return new Rate(1L, 1L, 4, "Хорошие суши");
    }
}