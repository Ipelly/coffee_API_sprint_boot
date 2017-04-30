package com.xiaoslab.coffee.api.utility;

import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.security.Roles;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

public class UserUtility {

    private static final byte NO_SHOP_ID = -1;

    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            if (auth.getPrincipal() != null && auth.getPrincipal() instanceof User) {
                return (User) auth.getPrincipal();
            }
        }
        return null;
    }

    public boolean isXipliAdmin() {
        return getLoggedInUser() != null && hasRole(Roles.ROLE_X_ADMIN);
    }

    public boolean isShopAdmin() {
        return getLoggedInUser() != null && hasRole(Roles.ROLE_SHOP_ADMIN);
    }

    public boolean isShopUser() {
        return getLoggedInUser() != null && (hasRole(Roles.ROLE_SHOP_USER) || hasRole(Roles.ROLE_SHOP_ADMIN));
    }

    public boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            if (auth.getPrincipal() != null && auth.getPrincipal() instanceof UserDetails) {
                for(GrantedAuthority authority : ((UserDetails) auth.getPrincipal()).getAuthorities()) {
                    if (authority.getAuthority().equalsIgnoreCase(role)) {
                        return true;
                    }
                }
                for(GrantedAuthority authority : auth.getAuthorities()) {
                    if (authority.getAuthority().equalsIgnoreCase(role)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public long getUserShopId() {
        User user = getLoggedInUser();
        if (user == null || user.getShopId() == null) {
            return NO_SHOP_ID;
        } else {
            return user.getShopId();
        }
    }

    public boolean canUserManageShop(Long shopId) {
        if (shopId == null) {
            return false;
        } else if (!isXipliAdmin() &&
                (!isShopAdmin() || !Objects.equals(shopId, getLoggedInUser().getShopId()))) {
            return false;
        }
        return true;
    }

    public void checkUserCanManageShop(Long shopId) {
        // make sure user can access the shop if user is not xipli admin
        if (!canUserManageShop(shopId)) {
            throw new AccessDeniedException("User not allowed to access shop with ID: " + shopId);
        }
    }

    public void checkUserCanGrantRoles(User user) {
        if (!isXipliAdmin()) {
            for (String role : user.getRoles()) {
                if (role.equalsIgnoreCase(Roles.ROLE_X_ADMIN)) {
                    throw new AccessDeniedException("User not allowed to grant role: " + Roles.ROLE_X_ADMIN);
                }
                if (role.equalsIgnoreCase(Roles.ROLE_USER)) {
                    throw new AccessDeniedException("User not allowed to grant role: " + Roles.ROLE_USER);
                }
            }
        }
    }

}
