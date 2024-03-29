package com.xiaoslab.coffee.api.security;

import com.xiaoslab.coffee.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class SecurityConfig {

    private static final String SERVER_RESOURCE_ID = "oauth2-server";

    private static InMemoryTokenStore tokenStore = new InMemoryTokenStore();

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfig extends ResourceServerConfigurerAdapter {

        @Autowired
        private SocialTokenFilter socialTokenFilter;

        @Autowired
        private FacebookAuthenticationProvider facebookAuthenticationProvider;

        @Autowired
        private GoogleAuthenticationProvider googleAuthenticationProvider;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources
                    .tokenStore(tokenStore)
                    .resourceId(SERVER_RESOURCE_ID)
                    .stateless(false);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .requestMatchers().and().authorizeRequests()
                    .antMatchers("/health").permitAll()
                    .antMatchers("/v1/status").permitAll()
                    .antMatchers("/v1/users/register").permitAll()
                    .antMatchers("/v1/users/password/reset").permitAll()
                    .antMatchers("/v1/users/password/update").permitAll()
                    .antMatchers("/swagger**").permitAll()
                    .antMatchers("/swagger-resources/**").permitAll()
                    .antMatchers("/v2/api-docs").permitAll()
                    .antMatchers("/**").authenticated()
                    .and()
                    .addFilterBefore(socialTokenFilter, AbstractPreAuthenticatedProcessingFilter.class)
                    .authenticationProvider(facebookAuthenticationProvider)
                    .authenticationProvider(googleAuthenticationProvider);

        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .authenticationManager(authenticationManager)
                    .tokenStore(tokenStore)
                    .approvalStoreDisabled();
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients
                    .inMemory()
                    .withClient("coffee-web")
                    .secret("xiaoslab")
                    .authorizedGrantTypes("authorization_code", "refresh_token","password")
                    .scopes("read,write")
                    .resourceIds(SERVER_RESOURCE_ID)
            ;
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        @Qualifier("userService")
        private UserService userService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Bean
        public XipliAuthenticationProvider xipliAuthenticationProvider() {
            XipliAuthenticationProvider xipliAuthenticationProvider = new XipliAuthenticationProvider();
            xipliAuthenticationProvider.setUserDetailsService(userService);
            xipliAuthenticationProvider.setPasswordEncoder(passwordEncoder);
            xipliAuthenticationProvider.setAuthoritiesMapper(authoritiesMapper());
            return xipliAuthenticationProvider;
        }

        @Bean
        public CustomAuthoritiesMapper authoritiesMapper() {
            return new CustomAuthoritiesMapper();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(xipliAuthenticationProvider());
        }

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

}


//https://www.googleapis.com/plus/v1/people/me/?access_token=
//https://graph.facebook.com/me/?access_token=