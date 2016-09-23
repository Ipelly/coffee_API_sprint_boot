package com.demazetech.coffee.api;

import com.demazetech.coffee.api.security.FacebookAuthenticationProvider;
import com.demazetech.coffee.api.security.GoogleAuthenticationProvider;
import com.demazetech.coffee.api.security.SocialTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityBeans {

    @Bean
    public SocialTokenFilter socialTokenFilter() {
        return new SocialTokenFilter();
    }

    @Bean
    public FacebookAuthenticationProvider facebookAuthenticationProvider() {
        return new FacebookAuthenticationProvider();
    }

    @Bean
    public GoogleAuthenticationProvider googleAuthenticationProvider() {
        return new GoogleAuthenticationProvider();
    }

}