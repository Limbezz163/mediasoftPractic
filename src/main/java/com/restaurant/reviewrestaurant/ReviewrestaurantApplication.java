package com.restaurant.reviewrestaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;



@EntityScan("com.restaurant.reviewrestaurant.entity")
@SpringBootApplication
public class ReviewrestaurantApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewrestaurantApplication.class, args);
	}
}