package com.xiaoslab.coffee.api.security;

import com.xiaoslab.coffee.api.objects.UserInfo;
import com.xiaoslab.coffee.api.services.FacebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class FacebookAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private FacebookService facebookService;

    @Override
    public boolean supports(final Class<?> authentication) {
        return FacebookToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(final Authentication authentication) {
        if (!(authentication instanceof FacebookToken)) {
            throw new AuthenticationServiceException("expecting a FacebookToken, got " + authentication);
        }

        try {
            // validate token
            User facebookProfile = facebookService.getProfileFromToken(authentication.getCredentials().toString());
            UserInfo userInfo = facebookService.getUserInfoFromFacebookProfile(facebookProfile);

            // put account into security context
            return new FacebookToken(userInfo, authentication.getCredentials(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationServiceException("Internal error occurred");
        }
    }
}