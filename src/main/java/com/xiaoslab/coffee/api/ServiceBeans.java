package com.xiaoslab.coffee.api;

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


    @Bean
    public IService<Shop, String> shopService() {
        return new ShopService();
    }

    @Bean
    public IService<Addon, String> addonService() {
        return new AddonService();
    }

    @Bean
    public IService<Ingredient, String> ingredientService() {
        return new IngredientService();
    }

    @Bean
    public IService<Item, String> itemService() {
        return new ItemService();
    }

    @Bean
    public IService<ItemOption, String> itemOptionService() {
        return new ItemOptionService();
    }

    @Bean
    public IService<ItemAddon, String> itemAddonService() {
        return new ItemAddonService();
    }

    @Bean
    public IService<ItemIngredient, String> itemIngredientService() {
        return new ItemIngredientService();
    }



}