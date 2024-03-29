package com.xiaoslab.coffee.api.utilities;

import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.security.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ServiceLoginUtils {

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

    public void loginAsShopAdmin(Shop shop) {
        loginAsShopAdmin(shop.getShopId());
    }

    public void loginAsShopAdmin(long shopId) {
        User user = testUtils.setupBasicUserObject(Roles.ROLE_SHOP_ADMIN, Roles.ROLE_SHOP_USER);
        user.setShopId(shopId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void loginAsShopUser(long shopId) {
        User user = testUtils.setupBasicUserObject(Roles.ROLE_SHOP_USER);
        user.setShopId(shopId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }
}