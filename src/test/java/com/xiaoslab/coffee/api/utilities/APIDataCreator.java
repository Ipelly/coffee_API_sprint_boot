package com.xiaoslab.coffee.api.utilities;

import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.security.Roles;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class APIDataCreator {

    @Autowired
    APIAdapter api;

    @Autowired
    TestUtils testUtils;

    public ResponseEntity validateCreation(ResponseEntity response) {
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        return response;
    }

    public Shop createShopHelper() {
        ResponseEntity<Shop> response = api.createShop(testUtils.setupShopObject());
        validateCreation(response);
        return response.getBody();
    }

    public User createShopUserHelper(long shopId) {
        User userToCreate = testUtils.setupBasicUserObject(Roles.ROLE_SHOP_USER);
        ResponseEntity<User> response = api.createShopUser(shopId, userToCreate);
        validateCreation(response);
        return response.getBody();
    }

    public User createShopAdminHelper(long shopId) {
        User userToCreate = testUtils.setupBasicUserObject(Roles.ROLE_SHOP_ADMIN);
        ResponseEntity<User> response = api.createShopUser(shopId, userToCreate);
        validateCreation(response);
        return response.getBody();
    }

    public Item createItemHelper(long shopId) {
        Item item = testUtils.setupItemObjectForShop(shopId);
        ResponseEntity<Item> response = api.createItem(shopId, item);
        validateCreation(response);
        return response.getBody();
    }
}