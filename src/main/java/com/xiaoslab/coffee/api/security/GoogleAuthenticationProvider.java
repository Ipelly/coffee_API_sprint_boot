package com.xiaoslab.coffee.api.security;

import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.services.GoogleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.social.google.api.userinfo.GoogleUserInfo;
import org.springframework.stereotype.Component;

@Component
public class GoogleAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private GoogleService googleService;

    @Override
    public boolean supports(final Class<?> authentication) {
        return GoogleToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(final Authentication authentication) {
        if (!(authentication instanceof GoogleToken)) {
            throw new AuthenticationServiceException("expecting a GoogleToken, got " + authentication);
        }

        try {
            // validate token
            GoogleUserInfo googleProfile = googleService.getProfileFromToken(authentication.getCredentials().toString());
            User userInfo = googleService.findOrCreateLocalUserFromGoogleProfile(googleProfile);

            // put account into security context
            return new GoogleToken(userInfo, authentication.getCredentials(), userInfo.getAuthorities());

        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationServiceException("Internal error occurred");
        }
    }
}