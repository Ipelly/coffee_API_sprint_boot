package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.*;
import com.xiaoslab.coffee.api.utility.Constants;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by ipeli on 10/02/17.
 */
public class ItemOptionAPITest extends _BaseAPITest {

    long shopIdForTest,cateGoryIdFortest1,itemIdFortest;

    @Test
    public void createAndGetAndListOfItemOption() throws Exception {

        ResponseEntity<List<ItemOption>> itemOptionListResponse;
        ResponseEntity<ItemOption> itemOptionResponse;

        preReqDataForItemOptionTest();


        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        // test-case: create new item option for the item1 by POST
        ItemOption createItemOption1 = itemOptionCreateWithAssertion();

        // test-case: list of Items by GET
        api.login(CUSTOMER_USER);

        itemOptionListResponse = api.listItemOption(shopIdForTest,itemIdFortest);
        assertEquals(HttpStatus.OK, itemOptionListResponse.getStatusCode());
        List<ItemOption> itemOptionList = itemOptionListResponse.getBody();
        assertEquals(1, itemOptionList.size());
        assertEquals(createItemOption1, itemOptionList.get(0));
    }

    @Test
    public void createItemOptionWithoutAuthorization() throws Exception {

        preReqDataForItemOptionTest();

        ResponseEntity<ItemOption> response;

        ItemOption itemOption1 = testUtils.setupItemOptionObject(itemIdFortest);
        response = api.createItemOption(itemOption1,shopIdForTest);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        api.login(CUSTOMER_USER);
        response = api.createItemOption(itemOption1,shopIdForTest);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    }

    @Test
    public void updateItemOptionWithoutAuthorization() throws Exception {

        ResponseEntity<ItemOption> response;
        preReqDataForItemOptionTest();


        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        // test-case: create new item option for the item1 by POST
        ItemOption createItemOption1 = itemOptionCreateWithAssertion();

        api.logout();


        createItemOption1.setName("Samll in Size");
        response = api.updateItemOption(createItemOption1,shopIdForTest,createItemOption1.getItemOptionId() );
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        api.login(CUSTOMER_USER);
        response = api.updateItemOption(createItemOption1,shopIdForTest,createItemOption1.getItemOptionId() );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        api.login(XIPLI_ADMIN);
        response = api.updateItemOption(createItemOption1,shopIdForTest,createItemOption1.getItemOptionId() );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    }

    @Test
    public void deleteItemOptionWithoutAuthorization() throws Exception {

        ResponseEntity<ItemOption> response;
        preReqDataForItemOptionTest();


        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        // test-case: create new item option for the item1 by POST
        ItemOption createItemOption1 = itemOptionCreateWithAssertion();

        api.logout();


        //createItemOption1.setName("Samll in Size");
        response = api.deleteItemOption(createItemOption1,shopIdForTest,createItemOption1.getItemOptionId());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        api.login(CUSTOMER_USER);
        response = api.deleteItemOption(createItemOption1,shopIdForTest,createItemOption1.getItemOptionId());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        api.login(XIPLI_ADMIN);
        response = api.deleteItemOption(createItemOption1,shopIdForTest,createItemOption1.getItemOptionId());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    }

    @Test
    public void createAndUpdateAndDeleteItemOptionId() throws Exception {

        ResponseEntity<ItemOption> response;

        preReqDataForItemOptionTest();


        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        // test-case: create new item option for the item1 by POST
        ItemOption createItemOption1 = itemOptionCreateWithAssertion();

        // test-case: update item option by PUT
        createItemOption1.setName("Samll in Size");
        response = api.updateItemOption(createItemOption1,shopIdForTest,createItemOption1.getItemOptionId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ItemOption updatedItemOption1 = response.getBody();
        assertNotNull(updatedItemOption1);
        assertEquals(createItemOption1, updatedItemOption1);
        assertThat(updatedItemOption1.getName(), is(equalTo("Samll in Size")));

        // test-case: Delete item option by PUT
        response = api.deleteItemOption(createItemOption1,shopIdForTest,createItemOption1.getItemOptionId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // test-case: verify shop is not returned new shop by POST
        response = api.getItemOption(shopIdForTest,itemIdFortest,createItemOption1.getItemOptionId ());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void notFoundItemOptionId() throws Exception {

        api.login(XIPLI_ADMIN);

        ResponseEntity<ItemOption> response;

        // test-case: GET
        response = api.getItemOption(shopIdForTest,itemIdFortest,Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // test-case: PUT
        response = api.updateItemOption(new ItemOption(),shopIdForTest,Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // test-case: DELETE
        response = api.deleteItemOption(new ItemOption(),shopIdForTest,Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private void preReqDataForItemOptionTest(){

        api.login(XIPLI_ADMIN);

        ResponseEntity<List<Shop>> listResponse;
        ResponseEntity<Shop> response;
        // test-case: create new shop by POST
        Shop shop1 = testUtils.setupShopObject();
        response = api.createShop(shop1);
        Shop createdShop1 = response.getBody();
        shopIdForTest = createdShop1.getShopId();

        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(createdShop1.getShopId());
        api.login(shopAdmin);

        ResponseEntity<Category> categoryResponse1;
        categoryResponse1 = api.createCategory(new Category("Iced C","Iced Coffee",shopIdForTest),shopIdForTest);
        Category createdCategory1 = categoryResponse1.getBody();
        cateGoryIdFortest1 = createdCategory1.getCategory_id();

        // test-case: create new item by POST
        ResponseEntity<List<Item>> itemListResponse;
        ResponseEntity<Item> itemResponse;
        Item item1 = new Item("Latte","Late Coffe", BigDecimal.valueOf(5.00), shopIdForTest, cateGoryIdFortest1, Constants.StatusCodes.ACTIVE);
        //Item item1 = testUtils.setupItemObject(createdShop1.getShopId());
        itemResponse = api.createItem(item1,shopIdForTest);
        Item createItem1 = itemResponse.getBody();

        itemIdFortest = createItem1.getitem_id();

        api.logout();

    }
    private ItemOption itemOptionCreateWithAssertion(){
        ResponseEntity<List<ItemOption>> itemOptionListResponse;
        ResponseEntity<ItemOption> itemOptionResponse;
        ItemOption itemOption1 = testUtils.setupItemOptionObject(itemIdFortest);

        itemOptionResponse = api.createItemOption(itemOption1,shopIdForTest);
        assertEquals(HttpStatus.CREATED, itemOptionResponse.getStatusCode());
        ItemOption createItemOption1 = itemOptionResponse.getBody();
        assertNotNull(createItemOption1);
        assertTrue(createItemOption1.getItemOptionId() > 0);
        itemOption1.setItemOptionId (createItemOption1.getItemOptionId());
        assertEquals(itemOption1, createItemOption1);
        return createItemOption1;
    }
}
