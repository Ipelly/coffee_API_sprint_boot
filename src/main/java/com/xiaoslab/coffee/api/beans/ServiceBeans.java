package com.xiaoslab.coffee.api.beans;

import com.xiaoslab.coffee.api.objects.*;
import com.xiaoslab.coffee.api.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceBeans {

    @Bean
    public UserService userService() {
        return new UserService();
    }

    @Bean
    public FacebookService facebookService() {
        return new FacebookService();
    }

    @Bean
    public GoogleService googleService() {
        return new GoogleService();
    }

    @Bean
    public IService<Shop> shopService() {
        return new ShopService();
    }

    @Bean
    public IService<Item> itemService() {
        return new ItemService();
    }

}