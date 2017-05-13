package com.xiaoslab.coffee.api.security;
import com.xiaoslab.coffee.api.objects.User;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Objects;

@Component
public class XipliAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    CustomAuthoritiesMapper customAuthoritiesMapper;

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