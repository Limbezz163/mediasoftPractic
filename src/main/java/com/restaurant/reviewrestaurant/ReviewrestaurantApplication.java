package com.restaurant.reviewrestaurant;

import com.restaurant.reviewrestaurant.entity.*;
import com.restaurant.reviewrestaurant.Repositories.RestaurantRepository;
import com.restaurant.reviewrestaurant.Repositories.RateRepository;
import com.restaurant.reviewrestaurant.Repositories.VisitorRepository;
import com.restaurant.reviewrestaurant.Services.RateService;
import com.restaurant.reviewrestaurant.Services.RestaurantService;
import com.restaurant.reviewrestaurant.Services.VisitorService;
import com.restaurant.reviewrestaurant.enums.CuisineType;
import com.restaurant.reviewrestaurant.enums.Gender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigDecimal;

@SpringBootApplication
public class ReviewrestaurantApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewrestaurantApplication.class, args);

	}
}