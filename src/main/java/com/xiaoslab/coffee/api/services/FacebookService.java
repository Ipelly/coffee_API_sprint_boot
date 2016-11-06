package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.security.ProviderUserToLocalUserBridge;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

public class FacebookService {

    @Autowired
    private ProviderUserToLocalUserBridge providerUserToLocalUserBridge;

    public Facebook getFacebook(String token) {
        return new FacebookTemplate(token);
    }

    public org.springframework.social.facebook.api.User getProfileFromToken(String token) {
        return getFacebook(token).userOperations().getUserProfile();
    }

    public User findOrCreateLocalUserFromFacebookProfile(org.springframework.social.facebook.api.User facebookProfile) {
        User user = new User();
        user.setEmailAddress(facebookProfile.getEmail());
        user.setFirstName(facebookProfile.getFirstName());
        user.setLastName(facebookProfile.getLastName());
        user.setProviderUserId(facebookProfile.getId());
        user.setProviderType(Constants.LoginProviderType.FACEBOOK);
        return providerUserToLocalUserBridge.findOrCreateLocalUserFromProviderUser(user);
    }
}
