package com.xiaoslab.coffee.api.utilities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestBeans {

    @Bean
    public ServiceLoginUtils loginUtils() {
        return new ServiceLoginUtils();
    }

    @Bean
    public APIDataCreator apiDataCreator() {
        return new APIDataCreator();
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
