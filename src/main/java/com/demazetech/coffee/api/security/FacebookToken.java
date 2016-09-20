package com.demazetech.coffee.api.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class FacebookToken extends UsernamePasswordAuthenticationToken {

    public FacebookToken(Object credentials) {
        super(null, credentials);
    }

    public FacebookToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public FacebookToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
