package com.xiaoslab.coffee.api.utilities;

import com.xiaoslab.coffee.api.objects.Shop;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class APITestUtils {

    @Autowired
    APIAdapter api;

    @Autowired
    TestUtils testUtils;

    public ResponseEntity validateCreation(ResponseEntity response) {
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        return response;
    }

    public Shop createShop() {
        ResponseEntity<Shop> response = api.createShop(testUtils.setupShopObject());
        validateCreation(response);
        return response.getBody();
    }

}
