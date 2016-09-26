package com.xiaoslab.coffee.api.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class GoogleToken extends UsernamePasswordAuthenticationToken {

    public GoogleToken(Object credentials) {
        super(null, credentials);
    }

    public GoogleToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public GoogleToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
