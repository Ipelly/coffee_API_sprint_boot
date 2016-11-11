package com.xiaoslab.coffee.api.utility;

import com.xiaoslab.coffee.api.objects.AppAuthority;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.security.Roles;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class UserUtility {

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

    public void checkUserCanAccessShop(Long shopId) {
        if (shopId == null) {
            throw new IllegalArgumentException("Shop ID must be specified");
        }
        // make sure user can access the shop if user is not xipli admin
        if (!isXipliAdmin() && shopId != getLoggedInUser().getShopId()) {
            throw new AccessDeniedException("User not allowed to access shop with ID: " + shopId);
        } else if (!isShopAdmin() && shopId != getLoggedInUser().getShopId()) {
            throw new AccessDeniedException("User not allowed to access shop with ID: " + shopId);
        }
    }

    public void checkUserCanGrantRoles(User user) {
        if (!isXipliAdmin()) {
            for (AppAuthority role : user.getRoles()) {
                if (role.getAuthority().equalsIgnoreCase(Roles.ROLE_X_ADMIN)) {
                    throw new AccessDeniedException("User not allowed to grant role: " + Roles.ROLE_X_ADMIN);
                }
                if (role.getAuthority().equalsIgnoreCase(Roles.ROLE_USER)) {
                    throw new AccessDeniedException("User not allowed to grant role: " + Roles.ROLE_USER);
                }
            }
        }
    }

}
