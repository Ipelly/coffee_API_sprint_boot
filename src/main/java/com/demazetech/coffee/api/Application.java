package com.demazetech.coffee.api;

import com.demazetech.coffee.api.dao.MenuItemDao;
import com.demazetech.coffee.api.services.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;

@SpringBootApplication(exclude = FlywayAutoConfiguration.class)
public class Application {

    @Autowired
    DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/v1/*").allowedOrigins("*");
            }
        };
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public MenuItemDao menuItemDao() {
        return new MenuItemDao();
    }

    @Bean
    public MenuItemService menuItemService() {
        return new MenuItemService();
    }

}