package com.xiaoslab.coffee.api.utility;

public class Constants {

    public enum StatusCodes {
        DELETED,  // 0
        ACTIVE,   // 1
        INACTIVE, // 2
        PENDING,  // 3
        SUSPENDED,// 4
    }

    public enum LoginProviderType {
        XIPLI,    // 0
        FACEBOOK, // 1
        GOOGLE,   // 2
        TWITTER,  // 3
    }

    public static final int LAT_LONG_SCALE = 4;
    public static final int LAT_LONG_PRECISION = 20;
    public static final int ITEM_PRICE_SCALE = 2;

    public static final String V1 = "/v1";
    public static final String STATUS_ENDPOINT = "/status";
    public static final String TOKEN_ENDPOINT = "/oauth/token";
    public static final String USER_ENDPOINT = "/users";
    public static final String SHOP_ENDPOINT = "/shops";
    public static final String ITEM_ENDPOINT = "/items";
    public static final String OPTION_ENDPOINT = "/options";
    public static final String ADDON_ENDPOINT = "/addons";
    public static final String INGREDIENT_ENDPOINT = "/ingredients";
    public static final String CATEGORY_ENDPOINT = "/categories";
}
