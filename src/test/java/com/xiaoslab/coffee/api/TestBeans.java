package com.xiaoslab.coffee.api;

import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.services.IService;
import com.xiaoslab.coffee.api.services.ItemService;
import com.xiaoslab.coffee.api.services.ShopService;
import com.xiaoslab.coffee.api.utilities.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestBeans {

    @Bean
    public IService<Shop> shopService() {
        return new ShopService();
    }

    @Bean
    public IService<Item> itemIService(){ return new ItemService();}

    @Bean
    public LoginUtils loginUtils() {
        return new LoginUtils();
    }

    @Bean
    public APITestUtils apiTestUtils() {
        return new APITestUtils();
    }

    @Bean
    public TestUtils TestUtils() {
        return new TestUtils();
    }

    @Bean
    public APIAdapter apiAdapter() {
        return new APIAdapter();
    }

}
