package com.xiaoslab.coffee.api.apis;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ipeli on 10/19/16.
 */
public class ShopAPITest extends _BaseAPITest {

    static final String SHOP_ENDPOINT = "/shops";

    @Test
    public void statusAsJsonString() throws Exception {
        ResponseEntity entity = get(SHOP_ENDPOINT, String.class);
        String response = (String) entity.getBody();
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals("{\"shops\":\"running\"}", response);
    }

    @Test
    public void statusAsHashMap() throws Exception {
        ResponseEntity entity = get(SHOP_ENDPOINT, HashMap.class);
        HashMap response = (HashMap) entity.getBody();
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertTrue(response.containsKey("status"));
        assertEquals(response.get("shops"), "running");
    }

}
