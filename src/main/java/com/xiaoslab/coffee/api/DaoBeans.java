package com.xiaoslab.coffee.api;

import com.xiaoslab.coffee.api.dao.MenuItemDao;
import com.xiaoslab.coffee.api.dao.ShopDao;
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

}