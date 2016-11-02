package com.xiaoslab.coffee.api.utility;

public class Constants {

    public enum StatusCodes {
        DELETED,  // 0
        ACTIVE,   // 1
        INACTIVE, // 2
        PENDING,  // 3
    }

    public enum SocialProviderType {
        XIPLI,    // 0
        FACEBOOK, // 1
        GOOGLE,   // 2
        TWITTER,  // 3
    }

    public static final int LAT_LONG_SCALE = 4;
    public static final int LAT_LONG_PRECISION = 20;

    public static final String V1 = "/v1";
    public static final String STATUS_ENDPOINT = "/status";
    public static final String USER_ENDPOINT = "/users";
    public static final String SHOP_ENDPOINT = "/shops";
    public static final String ITEM_ENDPOINT = "/items";

}
