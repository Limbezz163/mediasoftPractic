package com.restaurant.reviewrestaurant.exception;

public class DuplicateRateException extends RuntimeException {
    public DuplicateRateException(String message) {
        super(message);
    }
}