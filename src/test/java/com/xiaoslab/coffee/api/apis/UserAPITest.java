package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.security.Roles;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

public class UserAPITest extends _BaseAPITest {

    @Test
    public void createShopAdmin() throws Exception {
        api.login(XIPLI_ADMIN);
        Shop shop = apiTestUtils.createShop();
        User shopAdmin = apiTestUtils.setupBasicUserObject(Roles.ROLE_SHOP_ADMIN);
        shopAdmin.setShopId(shop.getShopId());
        ResponseEntity<User> response = api.createUser(shopAdmin);
        User createdUser = response.getBody();
        Assert.assertEquals(shop.getShopId(), createdUser.getShopId().longValue());

        // make sure the new user can login
        apiTestUtils.resetPassword(createdUser.getUserId());
        api.login(createdUser);
    }

}