package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.utility.Constants;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ipeli on 10/19/16.
 */
public class ItemAPITest extends _BaseAPITest {

    @Test
    public void createAndGetAndListItem() throws Exception {

        api.login(XIPLI_ADMIN);

        ResponseEntity<List<Shop>> listResponse;
        ResponseEntity<Shop> response;

        // test-case: create new shop by POST
        Shop shop1 = testUtils.setupShopObject();
        response = api.createShop(shop1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Shop createdShop1 = response.getBody();
        assertNotNull(createdShop1);
        assertTrue(createdShop1.getShopId() > 0);
        shop1.setShopId(createdShop1.getShopId());
        shop1.setStatus(Constants.StatusCodes.INACTIVE);
        assertEquals(shop1, createdShop1);

        User shopAdmin = testUtils.createShopAdminUser(createdShop1.getShopId());
        api.login(shopAdmin);

        // test-case: create new item by POST
        ResponseEntity<List<Item>> itemListResponse;
        ResponseEntity<Item> itemResponse;

        Item item1 = testUtils.setupItemObject(createdShop1.getShopId());
        itemResponse = api.createItem(item1);
        assertEquals(HttpStatus.CREATED, itemResponse.getStatusCode());
        Item createItem1 = itemResponse.getBody();
        assertNotNull(createItem1);
        assertTrue(createItem1.getItemId() > 0);
        item1.setItemId(createItem1.getItemId());
        assertEquals(item1, createItem1);

        // test-case: create another new item by POST
        ResponseEntity<Item> itemResponse2;

        Item item2 = testUtils.setupItemObject(createdShop1.getShopId());
        itemResponse2 = api.createItem(item2);
        assertEquals(HttpStatus.CREATED, itemResponse2.getStatusCode());
        Item createItem2 = itemResponse2.getBody();
        assertNotNull(createItem2);
        assertTrue(createItem2.getItemId() > 0);
        item2.setItemId(createItem2.getItemId());
        assertEquals(item2, createItem2);


        // test-case: list items by GET
        api.login(CUSTOMER_USER);

        itemListResponse = api.listItem(createdShop1.getShopId());
        assertEquals(HttpStatus.OK, itemListResponse.getStatusCode());
        List<Item> itemList = itemListResponse.getBody();
        assertEquals(2, itemList.size());
        assertEquals(createItem1, itemList.get(0));
        assertEquals(createItem2, itemList.get(1));

        // test-case: get individual item by GET
        itemResponse = api.getItem(item1, createItem1.getItemId());
        assertEquals(HttpStatus.OK, itemResponse.getStatusCode());
        Item gottenShop1 = itemResponse.getBody();
        assertEquals(createItem1, gottenShop1);
        itemResponse = api.getItem(item1,createItem2.getItemId());
        assertEquals(HttpStatus.OK, itemResponse.getStatusCode());
        Item gottenItem2 = itemResponse.getBody();
        assertEquals(createItem2, gottenItem2);

    }

    @Test
    public void createItemWithoutAuthorization() throws Exception {


        ResponseEntity<Item> response;

        // login as xipli admin and create new shop and shop-admin
        api.login(XIPLI_ADMIN);
        Shop newShop = apiTestUtils.createShop();
        Shop anotherShop = apiTestUtils.createShop();
        User anotherAdmin = testUtils.createShopAdminUser(anotherShop.getShopId());

        Item item1 = testUtils.setupItemObject(newShop.getShopId());
        response = api.createItem(item1);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        // Trying to create an item as a user
        api.login(CUSTOMER_USER);
        response = api.createItem(item1);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        // Trying to create an item as an admin of another shop


        api.login(anotherAdmin);
        response = api.createItem(item1);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        api.logout();
        response = api.createItem(item1);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }

    @Test
    public void updateItemWithoutAuthorization() throws Exception {
        ResponseEntity<Item> response;

        api.login(XIPLI_ADMIN);
        Shop newShop = apiTestUtils.createShop();
        Shop anotherShop = apiTestUtils.createShop();
        User anotherAdmin = testUtils.createShopAdminUser(anotherShop.getShopId());
        User shopAdmin = testUtils.createShopAdminUser(newShop.getShopId());

        api.login(shopAdmin);
        Item item1 = testUtils.setupItemObject(newShop.getShopId());
        response = api.createItem(item1);

        api.logout();
        response = api.updateItem(item1, item1.getItemId());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        api.login(CUSTOMER_USER);
        response = api.updateItem(item1, item1.getItemId());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        api.login(XIPLI_ADMIN);
        response = api.updateItem(item1, item1.getShopId());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

//        api.login(anotherAdmin);
//        response = api.updateItem(item1, item1.getItemId());
//        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());


   }

//    @Test
//    public void deleteItemWithoutAuthorization() throws Exception {
//        ResponseEntity<Item> response;
//
//        api.login(XIPLI_ADMIN);
//        Item item1 = testUtils.setupItemObject();
//        response = api.createItem(item1);
//        Item createdItem1 = response.getBody();
//        assertNotNull(createdItem1);
//        assertTrue(createdItem1.getItemId() > 0);
//        item1.setItemId(createdItem1.getItemId());
//        item1.setStatus(Constants.StatusCodes.INACTIVE);
//        assertEquals(item1, createdItem1);
//
//        api.logout();
//        response = api.deleteItem(createdItem1.getItemId());
//        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
//
//        api.login(CUSTOMER_USER);
//        response = api.deleteItem(createdItem1.getItemId());
//        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
//
//        api.login(XIPLI_ADMIN);
//        response = api.deleteItem(createdItem1.getItemId());
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//    }
//
//    @Test
//    public void createAndUpdateAndDeleteShop() throws Exception {
//
//        api.login(XIPLI_ADMIN);
//
//        ResponseEntity<Item> response;
//
//        // test-case: create new shop by POST
//        Item item1 = testUtils.setupItemObject();
//        response = api.createItem(item1);
//        Item createdItem1 = response.getBody();
//        assertNotNull(createdItem1);
//        assertTrue(createdItem1.getItemId() > 0);
//        item1.setItemId(createdItem1.getItemId());
//        item1.setStatus(Constants.StatusCodes.INACTIVE);
//        assertEquals(item1, createdItem1);
//
//        // test-case: update shop by PUT
//        createdItem1.setName("Test Item Updated Name");
//        response = api.updateItem(createdItem1.getItemId(), createdItem1);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Item updatedItem1 = response.getBody();
//        assertNotNull(updatedItem1);
//        assertEquals(createdItem1, updatedItem1);
//        assertThat(updatedItem1.getName(), is(equalTo("Test Item Updated Name")));
//
//        // test-case: delete shop by DELETE
//        response = api.deleteItem(createdItem1.getItemId());
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//
//        // test-case: verify shop is not returned new shop by POST
//        response = api.getItem(createdItem1.getItemId());
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//
//
//    @Test
//    public void notFoundShopId() throws Exception {
//
//        api.login(XIPLI_ADMIN);
//
//        ResponseEntity<Item> response;
//
//        // test-case: GET
//        response = api.getItem(Integer.MAX_VALUE);
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//
//        // test-case: PUT
//        response = api.updateItem(Integer.MAX_VALUE, new Item());
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//
//        // test-case: DELETE
//        response = api.deleteItem(Integer.MAX_VALUE);
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }

}
