package com.xiaoslab.coffee.api;

import com.xiaoslab.coffee.api.services.FacebookService;
import com.xiaoslab.coffee.api.services.GoogleService;
import com.xiaoslab.coffee.api.services.MenuItemService;
import com.xiaoslab.coffee.api.services.ShopService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceBeans {

    @Bean
    public MenuItemService menuItemService() {
        return new MenuItemService();
    }

    @Bean
    public ShopService shopService() {
        return new ShopService();
    }


    @Bean
    public FacebookService facebookService() {
        return new FacebookService();
    }

    @Bean
    public GoogleService googleService() {
        return new GoogleService();
    }

}