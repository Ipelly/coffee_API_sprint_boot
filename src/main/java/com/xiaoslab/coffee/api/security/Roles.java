package com.xiaoslab.coffee.api.security;

public class Roles {

    public static final String ROLE_USER = "ROLE_USER";             // any customer user
    public static final String ROLE_SHOP_USER = "ROLE_SHOP_USER";   // any vendor user - can manage his/her shop items
    public static final String ROLE_SHOP_ADMIN= "ROLE_SHOP_ADMIN";  // shop manager - can manage his/her shop user and items
    public static final String ROLE_X_ADMIN = "ROLE_X_ADMIN";       // xipli super admin - can manage shops and users

}
