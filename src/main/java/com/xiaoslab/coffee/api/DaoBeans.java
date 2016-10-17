package com.xiaoslab.coffee.api;

import com.xiaoslab.coffee.api.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DaoBeans {

    @Autowired
    DataSource dataSource;

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public MenuItemDao menuItemDao() {
        return new MenuItemDao();
    }


    @Bean
    public ShopDao shopDao() {
        return new ShopDao();
    }

    @Bean
    public AddonDao addonDaoDao() {
        return new AddonDao();
    }

    @Bean
    public IngredientDao ingredientDao() {
        return new IngredientDao();
    }

    @Bean
    public ItemAddonDao itemAddonDaoDao() {
        return new ItemAddonDao();
    }

    @Bean
    public ItemDao itemDao() {
        return new ItemDao();
    }

    @Bean
    public ItemOptionDao itemOptionDao() {
        return new ItemOptionDao();
    }

    @Bean
    public ItemIngredientDao itemIngredientDao() {
        return new ItemIngredientDao();
    }
}