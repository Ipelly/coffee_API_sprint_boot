package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.utilities.APIAdapter;
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

    @Test
    public void createAndGetAndListShop() throws Exception {

        api.login(XIPLI_ADMIN);

        ResponseEntity<List<Shop>> listResponse;
        ResponseEntity<Shop> response;

        // test-case: create new shop by POST
        Shop shop1 = testUtils.setupShopObject();
        shop1.setStatus(Constants.StatusCodes.INACTIVE);
        response = api.createShop(shop1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Shop createdShop1 = response.getBody();
        assertNotNull(createdShop1);
        assertTrue(createdShop1.getShopId() > 0);
        shop1.setShopId(createdShop1.getShopId());
        assertEquals(shop1, createdShop1);

        // test-case: list shops by GET
        api.login(CUSTOMER_USER);

        listResponse = api.listShop("?size=1000");
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        List<Shop> shopList = listResponse.getBody();
        assertEquals(1, shopList.size());
        assertEquals(createdShop1, shopList.get(0));

        // test-case: create another shop by POST
        api.login(XIPLI_ADMIN);

        Shop shop2 = testUtils.setupShopObject();
        shop2.setStatus(Constants.StatusCodes.ACTIVE);
        response = api.createShop(shop2);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Shop createdShop2 = response.getBody();
        assertNotNull(createdShop2);
        assertTrue(createdShop2.getShopId() > 0);
        shop2.setShopId(createdShop2.getShopId());
        assertEquals(shop2, createdShop2);

        // test-case: list shops by GET
        api.login(CUSTOMER_USER);

        listResponse = api.listShop("?size=1000");
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        shopList = listResponse.getBody();
        assertEquals(2, shopList.size());
        assertEquals(createdShop1, shopList.get(0));
        assertEquals(createdShop2, shopList.get(1));

        // test-case: get individual shops by GET
        response = api.getShop(createdShop1.getShopId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Shop gottenShop1 = response.getBody();
        assertEquals(createdShop1, gottenShop1);
        response = api.getShop(createdShop2.getShopId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Shop gottenShop2 = response.getBody();
        assertEquals(createdShop2, gottenShop2);

        // test search shop1 by address
        listResponse = api.listShop("?size=1000&search=" + shop1.getAddress1());
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        shopList = listResponse.getBody();
        assertEquals(1, shopList.size());
        assertEquals(createdShop1, shopList.get(0));

        // test search shop2 by name
        listResponse = api.listShop("?size=1000&search=" + shop2.getName());
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        shopList = listResponse.getBody();
        assertEquals(1, shopList.size());
        assertEquals(createdShop2, shopList.get(0));

    }

    @Test
    public void createShopWithoutAuthorization() throws Exception {
        ResponseEntity<Shop> response;

        Shop shop1 = testUtils.setupShopObject();
        response = api.createShop(shop1);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        api.login(CUSTOMER_USER);
        response = api.createShop(shop1);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    }

    @Test
    public void updateShopWithoutAuthorization() throws Exception {
        ResponseEntity<Shop> response;

        api.login(XIPLI_ADMIN);
        Shop shop1 = testUtils.setupShopObject();
        response = api.createShop(shop1);
        Shop createdShop1 = response.getBody();
        assertNotNull(createdShop1);
        assertTrue(createdShop1.getShopId() > 0);
        shop1.setShopId(createdShop1.getShopId());
        assertEquals(shop1, createdShop1);

        api.logout();
        response = api.updateShop(createdShop1.getShopId(), createdShop1);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        api.login(CUSTOMER_USER);
        response = api.updateShop(createdShop1.getShopId(), createdShop1);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        api.login(XIPLI_ADMIN);
        response = api.updateShop(createdShop1.getShopId(), createdShop1);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void deleteShopWithoutAuthorization() throws Exception {
        ResponseEntity<Shop> response;

        api.login(XIPLI_ADMIN);
        Shop shop1 = testUtils.setupShopObject();
        response = api.createShop(shop1);
        Shop createdShop1 = response.getBody();
        assertNotNull(createdShop1);
        assertTrue(createdShop1.getShopId() > 0);
        shop1.setShopId(createdShop1.getShopId());
        assertEquals(shop1, createdShop1);

        api.logout();
        response = api.deleteShop(createdShop1.getShopId());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        api.login(CUSTOMER_USER);
        response = api.deleteShop(createdShop1.getShopId());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        api.login(XIPLI_ADMIN);
        response = api.deleteShop(createdShop1.getShopId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void createAndUpdateAndDeleteShop() throws Exception {

        api.login(XIPLI_ADMIN);

        ResponseEntity<Shop> response;

        // test-case: create new shop by POST
        Shop shop1 = testUtils.setupShopObject();
        response = api.createShop(shop1);
        Shop createdShop1 = response.getBody();
        assertNotNull(createdShop1);
        assertTrue(createdShop1.getShopId() > 0);
        shop1.setShopId(createdShop1.getShopId());
        assertEquals(shop1, createdShop1);

        // test-case: update shop by PUT
        createdShop1.setName("Test Shop Updated Name");
        response = api.updateShop(createdShop1.getShopId(), createdShop1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Shop updatedShop1 = response.getBody();
        assertNotNull(updatedShop1);
        assertEquals(createdShop1, updatedShop1);
        assertThat(updatedShop1.getName(), is(equalTo("Test Shop Updated Name")));

        // test-case: delete shop by DELETE
        response = api.deleteShop(createdShop1.getShopId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // test-case: verify shop is not returned new shop by POST
        response = api.getShop(createdShop1.getShopId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void notFoundShopId() throws Exception {

        api.login(XIPLI_ADMIN);

        ResponseEntity<Shop> response;

        // test-case: GET
        response = api.getShop(Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // test-case: PUT
        response = api.updateShop(Integer.MAX_VALUE, new Shop());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // test-case: DELETE
        response = api.deleteShop(Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void listShopExceedMaxSize() throws Exception {
        api.login(XIPLI_ADMIN);
        ResponseEntity<List<Shop>> response;
        response = api.listShop("?size=1001");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains(api.getLastResponseAsString(), "Maximum page size can be 1000");
    }

    @Test
    public void listShopUserExceedMaxSize() throws Exception {
        api.login(XIPLI_ADMIN);
        ResponseEntity<List<User>> response;

        Shop shop1 = testUtils.setupShopObject();
        ResponseEntity<Shop> shopResponse = api.createShop(shop1);
        Shop createdShop1 = shopResponse.getBody();

        response = api.listShopUsers(createdShop1.getShopId(), "?size=1001");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains(api.getLastResponseAsString(), "Maximum page size can be 1000");
    }
}
