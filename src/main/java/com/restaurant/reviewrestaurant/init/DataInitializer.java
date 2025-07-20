package com.restaurant.reviewrestaurant.init;
import com.restaurant.reviewrestaurant.entity.*;
import com.restaurant.reviewrestaurant.Services.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final RestaurantService restaurantService;
    private final VisitorService visitorService;
    private final RateService rateService;
    private final Restaurant sushiWok;
    private final Restaurant italianCorner;
    private final Visitor ivan;
    private final Visitor anna;
    private final ObjectProvider<Rate> rateProvider;

    @Autowired
    public DataInitializer(RestaurantService restaurantService,
                           VisitorService visitorService,
                           RateService rateService,
                           @Qualifier("testRestaurant1") Restaurant sushiWok,
                           @Qualifier("testRestaurant2")Restaurant italianCorner,
                           @Qualifier("testVisitor1")Visitor ivan,
                           @Qualifier("testVisitor2")Visitor anna,
                           ObjectProvider<Rate> rateProvider) {
        this.restaurantService = restaurantService;
        this.visitorService = visitorService;
        this.rateService = rateService;
        this.sushiWok = sushiWok;
        this.italianCorner = italianCorner;
        this.ivan = ivan;
        this.anna = anna;
        this.rateProvider = rateProvider;
    }

    @PostConstruct
    public void init() {
        restaurantService.save(sushiWok);
        restaurantService.save(italianCorner);

        visitorService.save(ivan);
        visitorService.save(anna);

        Rate rate1 = rateProvider.getObject();
        Rate rate2 = rateProvider.getObject();
        rateService.save(rate1);
        rateService.save(rate2);
    }
}