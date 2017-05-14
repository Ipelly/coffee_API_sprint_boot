package com.xiaoslab.coffee.api.security;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.repository.ShopRepository;
import com.xiaoslab.coffee.api.services.ShopService;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;

@Component
public class XipliAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    CustomAuthoritiesMapper customAuthoritiesMapper;

    @Autowired
    ShopRepository shopRepository;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (userDetails instanceof User) {
            User user = (User) userDetails;
            if (user.getRoles().contains(Roles.ROLE_SHOP_USER) || user.getRoles().contains(Roles.ROLE_SHOP_ADMIN)) {
                if (user.getShopId() == null || user.getShopId() < 1) {
                    throw new BadCredentialsException("User is not assigned to any shop yet");
                } else {
                    Shop shop = shopRepository.findOne(user.getShopId());
                    if (shop == null || shop.getStatus() == Constants.StatusCodes.DELETED || shop.getStatus() == Constants.StatusCodes.SUSPENDED) {
                        throw new BadCredentialsException("The shop this user is assigned to is either deleted or suspended");
                    }
                }
            }
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        if (user instanceof User && principal instanceof User && !CollectionUtils.isEmpty(user.getAuthorities())) {
            Collection<? extends GrantedAuthority> authorities = customAuthoritiesMapper.mapAuthorities(user.getAuthorities());
            Collection<String> roles = new HashSet<>();
            for (GrantedAuthority authority : authorities) {
                roles.add(authority.getAuthority());
            }
            // user and principal is same here
            ((User)user).setRoles(roles);
            ((User)principal).setRoles(roles);
        }
        return super.createSuccessAuthentication(principal, authentication, user);
    }

}