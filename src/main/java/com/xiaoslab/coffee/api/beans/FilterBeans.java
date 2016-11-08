package com.xiaoslab.coffee.api.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

@Configuration
public class FilterBeans {

    public static class RequestLoggingFilter extends CommonsRequestLoggingFilter {

        @Override
        protected boolean shouldLog(HttpServletRequest request) {
            return logger.isInfoEnabled();
        }

        @Override
        protected void beforeRequest(HttpServletRequest request, String message) {
            logger.info(message);
        }

        @Override
        protected void afterRequest(HttpServletRequest request, String message) {
            // do not log
            // logger.info(message);
        }
    }

    @Bean
    public Filter logFilter() {
        RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
        requestLoggingFilter.setBeforeMessagePrefix("Request by ".concat(parseNameFromSecurityContext()).concat(" ["));
        requestLoggingFilter.setBeforeMessageSuffix("]");
        requestLoggingFilter.setIncludeQueryString(true);
        requestLoggingFilter.setIncludePayload(false);
        requestLoggingFilter.setMaxPayloadLength(5120);
        return requestLoggingFilter;
    }

    private String parseNameFromSecurityContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() != null) {
            if (auth.getPrincipal() instanceof UserDetails) {
                return ((UserDetails) auth.getPrincipal()).getUsername();
            } else {
                return auth.getPrincipal().toString();
            }
        }
        return "anonymous";
    }
}
