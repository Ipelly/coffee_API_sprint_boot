package com.demazetech.coffee.api.services;

import com.demazetech.coffee.api.objects.GlobalEnums.ProviderType;
import com.demazetech.coffee.api.objects.UserInfo;
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
        userInfo.setProviderType(ProviderType.GOOGLE);
        return userInfo;
    }
}
