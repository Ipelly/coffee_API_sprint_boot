package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.PasswordUpdateRequest;
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
        User shopAdmin = testUtils.setupBasicUserObject(Roles.ROLE_SHOP_ADMIN);
        shopAdmin.setShopId(shop.getShopId());
        ResponseEntity<User> response = api.createUser(shopAdmin);
        User createdUser = response.getBody();
        Assert.assertEquals(shop.getShopId(), createdUser.getShopId().longValue());

        // make sure the new user can login
        testUtils.resetPassword(createdUser.getUserId());
        api.login(createdUser);
    }

    @Test
    public void passwordReset() throws Exception {
        api.login(XIPLI_ADMIN);
        Shop shop = apiTestUtils.createShop();
        User shopAdmin = testUtils.setupBasicUserObject(Roles.ROLE_SHOP_ADMIN);
        shopAdmin.setShopId(shop.getShopId());
        ResponseEntity<User> userResponse = api.createUser(shopAdmin);
        User createdUser = userResponse.getBody();
        Assert.assertEquals(shop.getShopId(), createdUser.getShopId().longValue());

        PasswordUpdateRequest request = new PasswordUpdateRequest();
        request.setEmailAddress(createdUser.getEmailAddress());
        ResponseEntity response = api.passwordReset(request);

        // TODO: validations
    }


    @Test
    public void passwordUpdate() throws Exception {

        PasswordUpdateRequest request = new PasswordUpdateRequest();
        request.setEmailAddress("testuser1480480033346@xipli.com");
        request.setCode("ag18qfp65e-k5s2lpjlb9lk9l9j424h-7v6qm69i0n");
        request.setPassword("foo");
        ResponseEntity response = api.passwordUpdate(request);

        // TODO: validations

    }

}