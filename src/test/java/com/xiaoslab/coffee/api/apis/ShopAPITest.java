package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.services.IService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Created by ipeli on 10/19/16.
 */
public class ShopAPITest extends _BaseAPITest {

    static final String SHOP_ENDPOINT = "/shops";

    @Autowired
    private IService<Shop> shopService;


    @Test
    public void shopAsJsonString() throws Exception {

        // test-case: create new shop 1
        Shop shop = new Shop();
        shop.setName("DD");
        shop.setAddress1("165 Liberty Ave");
        shop.setAddress2(", Jersey City");
        shop.setCity("JC");
        shop.setState("NJ");
        shop.setZip("07306");
        shop.setPhone("6414517510");
        shop.setLatitude(new BigDecimal(40.7426));
        shop.setLongitute(new BigDecimal(-74.0623));
        shop.setRating(5);
        Shop createdUser = shopService.create(shop);


        ResponseEntity entity = get(SHOP_ENDPOINT, String.class);
        String response = (String) entity.getBody();
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    public void statusAsHashMap() throws Exception {

      /* ResponseEntity entity = get(SHOP_ENDPOINT, HashMap.class);
        HashMap response = (HashMap) entity.getBody();
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        //assertTrue(response.containsKey("status"));
        //assertEquals(response.get("shops"), "running");*/
    }

}
