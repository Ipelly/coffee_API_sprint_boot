package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.UserInfo;
import com.xiaoslab.coffee.api.objects.GlobalEnums;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.userinfo.GoogleUserInfo;

public class GoogleService {

    public Google getGoogle(String token) {
        return new GoogleTemplate(token);
    }

    public GoogleUserInfo getProfileFromToken(String token) {
        return getGoogle(token).userOperations().getUserInfo();
    }

    public UserInfo getUserInfoFromGoogleProfile(GoogleUserInfo googleProfile) {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(googleProfile.getEmail());
        userInfo.setFirstName(googleProfile.getFirstName());
        userInfo.setLastName(googleProfile.getLastName());
        userInfo.setProviderId(googleProfile.getId());
        userInfo.setProviderType(GlobalEnums.ProviderType.GOOGLE);
        return userInfo;
    }
}
