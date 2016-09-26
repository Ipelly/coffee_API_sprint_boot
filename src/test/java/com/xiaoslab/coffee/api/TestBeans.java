package com.xiaoslab.coffee.api;

import com.xiaoslab.coffee.api.utilities.LoginUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestBeans {

    @Bean
    public LoginUtils loginUtils() {
        return new LoginUtils();
    }

}
