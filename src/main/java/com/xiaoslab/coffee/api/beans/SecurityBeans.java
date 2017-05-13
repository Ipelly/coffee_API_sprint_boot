package com.xiaoslab.coffee.api.beans;

import com.xiaoslab.coffee.api.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeans {

    @Bean
    public SocialTokenFilter socialTokenFilter() {
        return new SocialTokenFilter();
    }

    @Bean
    public ProviderUserToLocalUserBridge providerUserToLocalUserBridge() {
        return new ProviderUserToLocalUserBridge();
    }

    @Bean
    public FacebookAuthenticationProvider facebookAuthenticationProvider() {
        return new FacebookAuthenticationProvider();
    }

    @Bean
    public GoogleAuthenticationProvider googleAuthenticationProvider() {
        return new GoogleAuthenticationProvider();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}