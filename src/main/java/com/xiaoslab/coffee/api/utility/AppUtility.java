package com.xiaoslab.coffee.api.utility;

import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created by ipeli on 10/15/16.
 */
public class AppUtility {

    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.notNull(requestAttributes, "Could not find current request via RequestContextHolder");
        Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        Assert.state(servletRequest != null, "Could not find current HttpServletRequest");
        return servletRequest;
    }

    public static URI buildCreatedLocation(Object createdId) {
        return buildCreatedLocation(getCurrentRequest().getServletPath(), createdId);
    }

    public static URI buildCreatedLocation(String rootPath, Object createdId) {
        while (rootPath.endsWith("/")) {
            rootPath = rootPath.substring(0, rootPath.lastIndexOf("/"));
        }
        return UriBuilder.fromPath(rootPath + "/{createdId}").build(createdId);
    }

    public static <T> T notFoundOnNull(T obj) {
        if (obj==null) {
            throw new EntityNotFoundException();
        } else {
            return obj;
        }
    }
    public static <T> Boolean isNullObject(T obj) {
        if (obj==null) {
            return true;
        } else {
            return false;
        }
    }
}
