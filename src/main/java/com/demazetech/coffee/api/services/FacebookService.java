package com.demazetech.coffee.api.services;

import com.demazetech.coffee.api.objects.GlobalEnums.ProviderType;
import com.demazetech.coffee.api.objects.UserInfo;
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
        userInfo.setProviderType(ProviderType.FACEBOOK);
        return userInfo;
    }
}
