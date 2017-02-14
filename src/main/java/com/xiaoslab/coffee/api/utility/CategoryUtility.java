package com.xiaoslab.coffee.api.utility;

import com.xiaoslab.coffee.api.objects.AppAuthority;
import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.security.Roles;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Objects;

public class CategoryUtility {

//    public boolean checkUserDeleteCategory(List<Item> items) {
//        // make sure user can access the shop if user is not xipli admin
//        if (items.size() > 0) {
//            return  false;
//        }
//        return true;
//    }
}
