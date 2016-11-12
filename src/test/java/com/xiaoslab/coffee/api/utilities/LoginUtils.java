package com.xiaoslab.coffee.api.utilities;

import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.security.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginUtils {

    @Autowired
    TestUtils testUtils;

    public void loginAsCustomerUser() {
        User user = testUtils.setupBasicUserObject(Roles.ROLE_USER);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void loginAsXAdmin() {
        User user = testUtils.setupBasicUserObject(Roles.ROLE_X_ADMIN);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void loginAsShopAdmin(long shopId) {
        User user = testUtils.setupBasicUserObject(Roles.ROLE_SHOP_ADMIN);
        user.setShopId(shopId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
