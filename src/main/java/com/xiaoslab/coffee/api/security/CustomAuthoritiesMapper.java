package com.xiaoslab.coffee.api.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hossain on 5/12/17.
 */
public class CustomAuthoritiesMapper implements GrantedAuthoritiesMapper {

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {

        if (CollectionUtils.isEmpty(authorities)) return authorities;

        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

        // add additional roles based on role hierarchy
        for (GrantedAuthority authority : authorities) {
            mappedAuthorities.add(authority);
            if (authority.getAuthority().equals(Roles.ROLE_SHOP_ADMIN)) {
                mappedAuthorities.add(new SimpleGrantedAuthority(Roles.ROLE_SHOP_USER));
                mappedAuthorities.add(new SimpleGrantedAuthority(Roles.ROLE_USER));
            }
            if (authority.getAuthority().equals(Roles.ROLE_X_ADMIN)) {
                mappedAuthorities.add(new SimpleGrantedAuthority(Roles.ROLE_USER));
            }
        }

        return mappedAuthorities;
    }

}
