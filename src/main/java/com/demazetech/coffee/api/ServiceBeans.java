package com.demazetech.coffee.api;

import com.demazetech.coffee.api.services.FacebookService;
import com.demazetech.coffee.api.services.GoogleService;
import com.demazetech.coffee.api.services.MenuItemService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceBeans {

    @Bean
    public MenuItemService menuItemService() {
        return new MenuItemService();
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