package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.utility.Constants;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by ipeli on 10/19/16.
 */
public class ShopAPITest extends _BaseAPITest {

    static final String V1_SHOP_ROOT_PATH = Constants.V1 + Constants.SHOP_ENDPOINT + "/";

    @Test
    public void createAndGetAndListShop() throws Exception {

        ResponseEntity<List<Shop>> listResponse;
        ResponseEntity<Shop> createdResponse;
        ResponseEntity<Shop> getResponse;

        // test-case: create new shop by POST
        Shop shop = new Shop();
        shop.setName("DD");
        shop.setAddress1("165 Liberty Ave");
        shop.setAddress2("Jersey City");
        shop.setCity("JC");
        shop.setState("NJ");
        shop.setZip("07306");
        shop.setPhone("6414517510");
        shop.setLatitude(new BigDecimal(40.7426));
        shop.setLongitude(new BigDecimal(-74.0623));
        shop.setRating(5);
        createdResponse = POST(V1_SHOP_ROOT_PATH, shop, Shop.class);
        assertEquals(HttpStatus.CREATED, createdResponse.getStatusCode());
        Shop createdShop = createdResponse.getBody();
        assertNotNull(createdShop);

        // test-case: list shops by GET
        listResponse = LIST(V1_SHOP_ROOT_PATH, Shop.class);
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        List<Shop> shopList = listResponse.getBody();
        assertEquals(1, shopList.size());
        assertEquals(createdShop, shopList.get(0));

        // test-case: create another shop by POST
        Shop anotherShop = new Shop();
        anotherShop.setName("My Coffee Shop");
        anotherShop.setAddress1("16633 Hillside Ave");
        anotherShop.setAddress2("Floor 1");
        anotherShop.setCity("Jamaica");
        anotherShop.setState("NY");
        anotherShop.setZip("11435");
        anotherShop.setPhone("987654321");
        anotherShop.setLatitude(new BigDecimal(45.7426));
        anotherShop.setLongitude(new BigDecimal(-70.0623));
        anotherShop.setRating(4);
        createdResponse = POST(V1_SHOP_ROOT_PATH, anotherShop, Shop.class);
        assertEquals(HttpStatus.CREATED, createdResponse.getStatusCode());
        Shop createdShop2 = createdResponse.getBody();
        assertNotNull(createdShop2);

        // test-case: list shops by GET
        listResponse = LIST(V1_SHOP_ROOT_PATH, Shop.class);
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        shopList = listResponse.getBody();
        assertEquals(2, shopList.size());
        assertEquals(createdShop, shopList.get(0));
        assertEquals(createdShop2, shopList.get(1));

        // test-case: get individual shops by GET
        getResponse = GET(V1_SHOP_ROOT_PATH + createdShop.getShopId(), Shop.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Shop gottenShop1 = getResponse.getBody();
        assertEquals(createdShop, gottenShop1);
        getResponse = GET(V1_SHOP_ROOT_PATH + createdShop2.getShopId(), Shop.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Shop gottenShop2 = getResponse.getBody();
        assertEquals(createdShop2, gottenShop2);
    }

    @Test
    public void createAndUpdateAndDeleteShop() throws Exception {
        //TODO
    }

}
