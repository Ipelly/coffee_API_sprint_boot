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

//    public static Hashtable<String, Object> getFields(Object object){
//        Class theClass = object.getClass();
//        Hashtable<String, Object> privateFields = new Hashtable<String, Object>();
//
//        Field[] fields = theClass.getDeclaredFields();
//
//        for (Field field : fields) {
//            Object value = new Object();
//            String fieldName = field.getName().substring(0,1).toUpperCase().toString() + field.getName().substring(1,field.getName().length());
//            try {
//                Method m = object.getClass().getMethod("get" + fieldName, new Class[] {});
//                if(!m.invoke(object).equals(null)) {
//                    value = m.invoke(object);
//                }
//            }
//            catch (NoSuchMethodException e){
//                continue;
//            }
//            catch (InvocationTargetException e){
//
//                continue;
//            }
//            catch (IllegalAccessException e){
//                continue;
//            }
//
//            privateFields.put(fieldName, value);
//        }
//        return privateFields;
//    }

    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.notNull(requestAttributes, "Could not find current request via RequestContextHolder");
        Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        Assert.state(servletRequest != null, "Could not find current HttpServletRequest");
        return servletRequest;
    }

    public static URI buildCreatedLocation(long createdId) {
        return buildCreatedLocation(getCurrentRequest().getServletPath(), createdId);
    }

    public static URI buildCreatedLocation(String rootPath, long createdId) {
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
}
