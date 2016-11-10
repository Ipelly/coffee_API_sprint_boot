package com.xiaoslab.coffee.api.beans;

import com.xiaoslab.coffee.api.utility.UserUtility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilityBeans {

    @Bean
    public UserUtility userUtility() {
        return new UserUtility();
    }

}
