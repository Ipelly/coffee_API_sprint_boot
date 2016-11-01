package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.security.ProviderUserToLocalUserBridge;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.userinfo.GoogleUserInfo;

public class GoogleService {

    @Autowired
    private ProviderUserToLocalUserBridge providerUserToLocalUserBridge;

    public Google getGoogle(String token) {
        return new GoogleTemplate(token);
    }

    public GoogleUserInfo getProfileFromToken(String token) {
        return getGoogle(token).userOperations().getUserInfo();
    }

    public User findOrCreateLocalUserFromGoogleProfile(GoogleUserInfo googleProfile) {
        User user = new User();
        user.setEmailAddress(googleProfile.getEmail());
        user.setFirstName(googleProfile.getFirstName());
        user.setLastName(googleProfile.getLastName());
        user.setProviderUserId(googleProfile.getId());
        user.setProviderType(Constants.SocialProviderType.GOOGLE);
        return providerUserToLocalUserBridge.findOrCreateLocalUserFromProviderUser(user);
    }
}
