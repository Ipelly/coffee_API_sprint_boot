package com.xiaoslab.coffee.api.utilities;

import com.xiaoslab.coffee.api.security.Roles;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;

public class LoginUtils {

    public void loginAsCustomerUser() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "testuser","testpassword", Arrays.asList(new SimpleGrantedAuthority(Roles.ROLE_USER)));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void loginAsXAdmin() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "X-Admin","testpassword", Arrays.asList(new SimpleGrantedAuthority(Roles.ROLE_X_ADMIN)));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void loginAsShopAdmin() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "X-Admin","testpassword", Arrays.asList(new SimpleGrantedAuthority(Roles.ROLE_SHOP_ADMIN)));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
