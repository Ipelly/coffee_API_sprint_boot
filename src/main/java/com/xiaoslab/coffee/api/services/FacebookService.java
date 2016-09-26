package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.UserInfo;
import com.xiaoslab.coffee.api.objects.GlobalEnums;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

public class FacebookService {

    public Facebook getFacebook(String token) {
        return new FacebookTemplate(token);
    }

    public User getProfileFromToken(String token) {
        return getFacebook(token).userOperations().getUserProfile();
    }

    public UserInfo getUserInfoFromFacebookProfile(User facebookProfile) {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(facebookProfile.getEmail());
        userInfo.setFirstName(facebookProfile.getFirstName());
        userInfo.setLastName(facebookProfile.getLastName());
        userInfo.setProviderId(facebookProfile.getId());
        userInfo.setProviderType(GlobalEnums.ProviderType.FACEBOOK);
        return userInfo;
    }
}
