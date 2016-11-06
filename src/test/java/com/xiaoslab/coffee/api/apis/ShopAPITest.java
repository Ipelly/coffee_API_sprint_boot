package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.utility.Constants;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by ipeli on 10/19/16.
 */
public class ShopAPITest extends _BaseAPITest {

    static final String V1_SHOP_ROOT_PATH = Constants.V1 + Constants.SHOP_ENDPOINT + "/";

    @Test
    public void createAndGetAndListShop() throws Exception {

        loginWithOAuth2(XIPLI_ADMIN);

        ResponseEntity<List<Shop>> listResponse;
        ResponseEntity<Shop> response;

        // test-case: create new shop by POST
        Shop shop1 = apiTestUtils.setupShopObject();
        response = POST(V1_SHOP_ROOT_PATH, shop1, Shop.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Shop createdShop1 = response.getBody();
        assertNotNull(createdShop1);
        assertTrue(createdShop1.getShopId() > 0);
        shop1.setShopId(createdShop1.getShopId());
        shop1.setStatus(Constants.StatusCodes.INACTIVE);
        assertEquals(shop1, createdShop1);

        // test-case: list shops by GET
        loginWithOAuth2(CUSTOMER_USER);

        listResponse = LIST(V1_SHOP_ROOT_PATH, Shop.class);
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        List<Shop> shopList = listResponse.getBody();
        assertEquals(1, shopList.size());
        assertEquals(createdShop1, shopList.get(0));

        // test-case: create another shop by POST
        loginWithOAuth2(XIPLI_ADMIN);

        Shop shop2 = apiTestUtils.setupShopObject();
        response = POST(V1_SHOP_ROOT_PATH, shop2, Shop.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Shop createdShop2 = response.getBody();
        assertNotNull(createdShop2);
        assertTrue(createdShop2.getShopId() > 0);
        shop2.setShopId(createdShop2.getShopId());
        shop2.setStatus(Constants.StatusCodes.INACTIVE);
        assertEquals(shop2, createdShop2);

        // test-case: list shops by GET
        loginWithOAuth2(CUSTOMER_USER);

        listResponse = LIST(V1_SHOP_ROOT_PATH, Shop.class);
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        shopList = listResponse.getBody();
        assertEquals(2, shopList.size());
        assertEquals(createdShop1, shopList.get(0));
        assertEquals(createdShop2, shopList.get(1));

        // test-case: get individual shops by GET
        response = GET(V1_SHOP_ROOT_PATH + createdShop1.getShopId(), Shop.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Shop gottenShop1 = response.getBody();
        assertEquals(createdShop1, gottenShop1);
        response = GET(V1_SHOP_ROOT_PATH + createdShop2.getShopId(), Shop.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Shop gottenShop2 = response.getBody();
        assertEquals(createdShop2, gottenShop2);

        // test search shop1 by address
        listResponse = LIST(V1_SHOP_ROOT_PATH + "?search=" + shop1.getAddress1(), Shop.class);
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        shopList = listResponse.getBody();
        assertEquals(1, shopList.size());
        assertEquals(createdShop1, shopList.get(0));

        // test search shop2 by name
        listResponse = LIST(V1_SHOP_ROOT_PATH + "?search=" + shop2.getName(), Shop.class);
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        shopList = listResponse.getBody();
        assertEquals(1, shopList.size());
        assertEquals(createdShop2, shopList.get(0));

    }

    @Test
    public void createShopWithoutAuthorization() throws Exception {
        ResponseEntity<Shop> response;

        Shop shop1 = apiTestUtils.setupShopObject();
        response = POST(V1_SHOP_ROOT_PATH, shop1, Shop.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        loginWithOAuth2(CUSTOMER_USER);
        response = POST(V1_SHOP_ROOT_PATH, shop1, Shop.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    }

    @Test
    public void updateShopWithoutAuthorization() throws Exception {
        ResponseEntity<Shop> response;

        loginWithOAuth2(XIPLI_ADMIN);
        Shop shop1 = apiTestUtils.setupShopObject();
        response = POST(V1_SHOP_ROOT_PATH, shop1, Shop.class);
        Shop createdShop1 = response.getBody();
        assertNotNull(createdShop1);
        assertTrue(createdShop1.getShopId() > 0);
        shop1.setShopId(createdShop1.getShopId());
        shop1.setStatus(Constants.StatusCodes.INACTIVE);
        assertEquals(shop1, createdShop1);

        logoutFromOAuth2();
        response = PUT(V1_SHOP_ROOT_PATH + createdShop1.getShopId(), createdShop1, Shop.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        loginWithOAuth2(CUSTOMER_USER);
        response = PUT(V1_SHOP_ROOT_PATH + createdShop1.getShopId(), createdShop1, Shop.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        loginWithOAuth2(XIPLI_ADMIN);
        response = PUT(V1_SHOP_ROOT_PATH + createdShop1.getShopId(), createdShop1, Shop.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void deleteShopWithoutAuthorization() throws Exception {
        ResponseEntity<Shop> response;

        loginWithOAuth2(XIPLI_ADMIN);
        Shop shop1 = apiTestUtils.setupShopObject();
        response = POST(V1_SHOP_ROOT_PATH, shop1, Shop.class);
        Shop createdShop1 = response.getBody();
        assertNotNull(createdShop1);
        assertTrue(createdShop1.getShopId() > 0);
        shop1.setShopId(createdShop1.getShopId());
        shop1.setStatus(Constants.StatusCodes.INACTIVE);
        assertEquals(shop1, createdShop1);

        logoutFromOAuth2();
        response = DELETE(V1_SHOP_ROOT_PATH + createdShop1.getShopId(), Shop.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        loginWithOAuth2(CUSTOMER_USER);
        response = DELETE(V1_SHOP_ROOT_PATH + createdShop1.getShopId(), Shop.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        loginWithOAuth2(XIPLI_ADMIN);
        response = DELETE(V1_SHOP_ROOT_PATH + createdShop1.getShopId(), Shop.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void createAndUpdateAndDeleteShop() throws Exception {

        loginWithOAuth2(XIPLI_ADMIN);

        ResponseEntity<Shop> response;

        // test-case: create new shop by POST
        Shop shop1 = apiTestUtils.setupShopObject();
        response = POST(V1_SHOP_ROOT_PATH, shop1, Shop.class);
        Shop createdShop1 = response.getBody();
        assertNotNull(createdShop1);
        assertTrue(createdShop1.getShopId() > 0);
        shop1.setShopId(createdShop1.getShopId());
        shop1.setStatus(Constants.StatusCodes.INACTIVE);
        assertEquals(shop1, createdShop1);

        // test-case: update shop by PUT
        createdShop1.setName("Test Shop Updated Name");
        response = PUT(V1_SHOP_ROOT_PATH + createdShop1.getShopId(), createdShop1, Shop.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Shop updatedShop1 = response.getBody();
        assertNotNull(updatedShop1);
        assertEquals(createdShop1, updatedShop1);
        assertThat(updatedShop1.getName(), is(equalTo("Test Shop Updated Name")));

        // test-case: delete shop by DELETE
        response = DELETE(V1_SHOP_ROOT_PATH + createdShop1.getShopId(), Shop.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // test-case: verify shop is not returned new shop by POST
        response = GET(V1_SHOP_ROOT_PATH + createdShop1.getShopId(), Shop.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void notFoundShopId() throws Exception {

        loginWithOAuth2(XIPLI_ADMIN);

        ResponseEntity<Shop> response;

        // test-case: GET
        response = GET(V1_SHOP_ROOT_PATH + Integer.MAX_VALUE, Shop.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // test-case: PUT
        response = PUT(V1_SHOP_ROOT_PATH + Integer.MAX_VALUE, new Shop(), Shop.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // test-case: DELETE
        response = DELETE(V1_SHOP_ROOT_PATH + Integer.MAX_VALUE, Shop.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
