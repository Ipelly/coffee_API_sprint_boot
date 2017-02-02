package com.xiaoslab.coffee.api.beans;

import com.xiaoslab.coffee.api.objects.*;
import com.xiaoslab.coffee.api.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceBeans {

    @Bean
    public PasswordResetService passwordResetService() {
        return new PasswordResetService();
    }

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

    @Bean
    public IService<ItemOption> itemOptionService() {
        return new ItemOptionService();
    }

    @Bean
    public IService<Addon> addonService() { return new AddonService ();
    }

    @Bean
    public IService<Ingredient> ingredientService() { return new IngredientService ();
    }

    @Bean
    public IService<Category> categoryService() { return new CategoryService ();
    }
}