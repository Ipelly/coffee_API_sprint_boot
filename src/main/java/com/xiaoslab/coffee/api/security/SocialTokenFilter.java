package com.xiaoslab.coffee.api.security;

import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;

public class SocialTokenFilter extends GenericFilterBean {

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            if (isAuthenticationRequired()) {
                // extract token from header
                String[] social_token = extractToken((HttpServletRequest)request);
                Authentication authentication = null;

                if (social_token != null) {
                    String access_token = social_token[1];
                    if (social_token[0].equalsIgnoreCase(Constants.LoginProviderType.FACEBOOK.name())) {
                        authentication = new FacebookToken(access_token);
                    } else if (social_token[0].equalsIgnoreCase(Constants.LoginProviderType.GOOGLE.name())) {
                        authentication = new GoogleToken(access_token);
                    }
                }

                // dump token into security context (for authentication-provider to pick up)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.debug("session already contained valid Authentication - not checking again");
            }
        }
        chain.doFilter(request, response);
    }
    
    private String[] extractToken(HttpServletRequest request) {
        final String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Basic")) {
            // Authorization: Basic base64credentials
            try {
                String base64Credentials = authorization.substring("Basic".length()).trim();
                String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
                final String[] values = credentials.split(":", 2);
                if (values.length == 2) {
                    // values[0] --> provider
                    // values[1] --> access_token
                    return values;
                }
            } catch (Exception ex) {
                logger.warn(ex.getMessage());
            }
        }
        return null;
    }

    private boolean isAuthenticationRequired() {
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if ((existingAuth == null) || !existingAuth.isAuthenticated()) {
            return true;
        }
        return false;
    }
}
