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
        Shop ddShop = new Shop();
        ddShop.setName("DD");
        ddShop.setAddress1("165 Liberty Ave");
        ddShop.setAddress2("Jersey City");
        ddShop.setCity("JC");
        ddShop.setState("NJ");
        ddShop.setZip("07306");
        ddShop.setPhone("6414517510");
        ddShop.setLatitude(new BigDecimal(40.7426));
        ddShop.setLongitude(new BigDecimal(-74.0623));
        ddShop.setRating(5);
        createdResponse = POST(V1_SHOP_ROOT_PATH, ddShop, Shop.class);
        assertEquals(HttpStatus.CREATED, createdResponse.getStatusCode());
        Shop createdDDShop = createdResponse.getBody();
        assertNotNull(createdDDShop);

        // test-case: list shops by GET
        listResponse = LIST(V1_SHOP_ROOT_PATH, Shop.class);
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        List<Shop> shopList = listResponse.getBody();
        assertEquals(1, shopList.size());
        assertEquals(createdDDShop, shopList.get(0));

        // test-case: create another shop by POST
        Shop hillsideShop = new Shop();
        hillsideShop.setName("My Coffee Shop");
        hillsideShop.setAddress1("16633 Hillside Ave");
        hillsideShop.setAddress2("Floor 1");
        hillsideShop.setCity("Jamaica");
        hillsideShop.setState("NY");
        hillsideShop.setZip("11435");
        hillsideShop.setPhone("987654321");
        hillsideShop.setLatitude(new BigDecimal(45.7426));
        hillsideShop.setLongitude(new BigDecimal(-70.0623));
        hillsideShop.setRating(4);
        createdResponse = POST(V1_SHOP_ROOT_PATH, hillsideShop, Shop.class);
        assertEquals(HttpStatus.CREATED, createdResponse.getStatusCode());
        Shop createdHillsideShop = createdResponse.getBody();
        assertNotNull(createdHillsideShop);

        // test-case: list shops by GET
        listResponse = LIST(V1_SHOP_ROOT_PATH, Shop.class);
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        shopList = listResponse.getBody();
        assertEquals(2, shopList.size());
        assertEquals(createdDDShop, shopList.get(0));
        assertEquals(createdHillsideShop, shopList.get(1));

        // test-case: get individual shops by GET
        getResponse = GET(V1_SHOP_ROOT_PATH + createdDDShop.getShopId(), Shop.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Shop gottenShop1 = getResponse.getBody();
        assertEquals(createdDDShop, gottenShop1);
        getResponse = GET(V1_SHOP_ROOT_PATH + createdHillsideShop.getShopId(), Shop.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Shop gottenShop2 = getResponse.getBody();
        assertEquals(createdHillsideShop, gottenShop2);

        // test search hillside
        listResponse = LIST(V1_SHOP_ROOT_PATH + "?search=Hillside", Shop.class);
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        shopList = listResponse.getBody();
        assertEquals(1, shopList.size());
        assertEquals(createdHillsideShop, shopList.get(0));

        // test search dd
        listResponse = LIST(V1_SHOP_ROOT_PATH + "?search=Dd", Shop.class);
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        shopList = listResponse.getBody();
        assertEquals(1, shopList.size());
        assertEquals(createdDDShop, shopList.get(0));
    }

    @Test
    public void createAndUpdateAndDeleteShop() throws Exception {
        //TODO
    }

}
