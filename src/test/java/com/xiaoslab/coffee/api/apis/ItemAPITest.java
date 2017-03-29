package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.*;
import com.xiaoslab.coffee.api.utility.Constants;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by ipeli on 10/02/17.
 */
public class ItemAPITest extends _BaseAPITest {

    long shopIdForTest,cateGoryIdFortest1, cateGoryIdFortest2,itemIdFortest;
    Category cat1 = new Category();
    Category cat2 = new Category();

    @Test
    public void createAndGetAndListOfItemForFirstCategory() throws Exception {

        ResponseEntity<List<Item>> itemListResponse;
        ResponseEntity<Item> itemResponse;

        List<Category> cats = preReqDataForItemTestWithCate();

        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        Item createItem = ItemCreateWithAssertion(cats);

        api.login(CUSTOMER_USER);

        itemListResponse = api.listItem(shopIdForTest,cateGoryIdFortest1);
        assertEquals(HttpStatus.OK, itemListResponse.getStatusCode());
        List<Item> ItemList = itemListResponse.getBody();
        assertEquals(1, ItemList.size());
        assertEquals("Latte", ItemList.get(0).getName());
    }

    @Test
    public void createAndGetAndListOfItemFor2ndCategory() throws Exception {

        ResponseEntity<List<Item>> itemListResponse;
        ResponseEntity<Item> itemResponse;

        preReqDataForItemTest();

        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        //Item createItem = ItemCreateWithAssertion();
        Item createItem =  ItemCreateWithAssertion(new ArrayList<>());
        api.login(CUSTOMER_USER);

        itemListResponse = api.listItem(shopIdForTest,cateGoryIdFortest2);
        assertEquals(HttpStatus.OK, itemListResponse.getStatusCode());
        List<Item> ItemList2 = itemListResponse.getBody();
        assertEquals(1, ItemList2.size());
        assertEquals("Hot Latte", ItemList2.get(0).getName());
    }

    @Test
    public void createAndGetAndListOfItemForShop() throws Exception {

        ResponseEntity<List<Item>> itemListResponse;
        ResponseEntity<Item> itemResponse;

        preReqDataForItemTest();

        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        Item createItem = ItemCreateWithAssertion();

        api.login(CUSTOMER_USER);

        itemListResponse = api.listItem(shopIdForTest);
        assertEquals(HttpStatus.OK, itemListResponse.getStatusCode());
        List<Item> ItemList = itemListResponse.getBody();
        assertEquals(2, ItemList.size());
    }

    @Test
    public void createItemWithoutAuthorization() throws Exception {

        preReqDataForItemTest();

        ResponseEntity<Item> ItemResponse;

        Item item = new Item("Latte","Late Coffe", BigDecimal.valueOf(5.00), shopIdForTest, cateGoryIdFortest1, Constants.StatusCodes.ACTIVE);

        ItemResponse = api.createItem(item,shopIdForTest);
        assertEquals(HttpStatus.UNAUTHORIZED, ItemResponse.getStatusCode());

        api.login(CUSTOMER_USER);
        ItemResponse = api.createItem(item,shopIdForTest);
        assertEquals(HttpStatus.FORBIDDEN, ItemResponse.getStatusCode());

    }

    @Test
    public void updateItemWithoutAuthorization() throws Exception {

        ResponseEntity<Item> itemResponse;
        preReqDataForItemTest();


        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        Item createdItem = ItemCreateWithAssertion();

        api.logout();


        createdItem.setName("Iced Coffee");
        itemResponse = api.updateItem(createdItem,createdItem.getItemId());
        assertEquals(HttpStatus.UNAUTHORIZED, itemResponse.getStatusCode());

        api.login(CUSTOMER_USER);
        itemResponse = api.updateItem(createdItem,createdItem.getItemId());
        assertEquals(HttpStatus.FORBIDDEN, itemResponse.getStatusCode());

        api.login(XIPLI_ADMIN);
        itemResponse = api.updateItem(createdItem,createdItem.getItemId());
        assertEquals(HttpStatus.FORBIDDEN, itemResponse.getStatusCode());

    }

    @Test
    public void deleteItemOptionWithoutAuthorization() throws Exception {

        ResponseEntity<Item> itemResponse;

        preReqDataForItemTest();


        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        Item createdItem = ItemCreateWithAssertion();

        api.logout();

        itemResponse = api.deleteItem(createdItem,createdItem.getItemId());
        assertEquals(HttpStatus.UNAUTHORIZED, itemResponse.getStatusCode());

        api.login(CUSTOMER_USER);
        itemResponse = api.deleteItem(createdItem,createdItem.getItemId());
        assertEquals(HttpStatus.FORBIDDEN, itemResponse.getStatusCode());

        api.login(XIPLI_ADMIN);
        itemResponse = api.deleteItem(createdItem,createdItem.getItemId());
        assertEquals(HttpStatus.FORBIDDEN, itemResponse.getStatusCode());

    }

    @Test
    public void createAndUpdateAndDeleteItem() throws Exception {

        ResponseEntity<Item> ItemResponse;

        preReqDataForItemTest();


        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        Item createdItem = ItemCreateWithAssertion();


        createdItem.setName("Iced Coffee");
        ItemResponse = api.updateItem(createdItem,createdItem.getItemId());
        assertEquals(HttpStatus.OK, ItemResponse.getStatusCode());
        Item updatedItem = ItemResponse.getBody();
        assertNotNull(updatedItem);
        assertEquals(createdItem, updatedItem);
        assertThat(updatedItem.getName(), is(equalTo("Iced Coffee")));

        ItemResponse = api.deleteItem(createdItem,createdItem.getItemId());

        ItemResponse = api.getItem(createdItem,createdItem.getItemId ());
        assertEquals(HttpStatus.NOT_FOUND, ItemResponse.getStatusCode());
    }

    @Test
    public void notFoundItemId() throws Exception {

        api.login(XIPLI_ADMIN);

        ResponseEntity<Item> response;

        preReqDataForItemTest();

        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        Item item = new Item("","", BigDecimal.valueOf(5.00), shopIdForTest, cateGoryIdFortest1, Constants.StatusCodes.ACTIVE);
        // test-case: GET
        response = api.getItem(item,Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // test-case: PUT
        response = api.updateItem(item,Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // test-case: DELETE
        response = api.deleteItem(item,Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private void preReqDataForItemTest(){

        api.login(XIPLI_ADMIN);

        ResponseEntity<List<Shop>> shopListResponse;
        ResponseEntity<Shop> shopResponse;
        //List<Category> categories = new ArrayList<Category>();

        // test-case: create new shop and Category by POST
        Shop shop1 = testUtils.setupShopObject();
        shopResponse = api.createShop(shop1);
        Shop createdShop1 = shopResponse.getBody();
        shopIdForTest = createdShop1.getShopId();

        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        ResponseEntity<Category> categoryResponse1;
        categoryResponse1 = api.createCategory(new Category("Iced C","Iced Coffee",shopIdForTest),shopIdForTest);
        Category createdCategory1 = categoryResponse1.getBody();
        //categories.add(createdCategory1);
        cateGoryIdFortest1 = createdCategory1.getCategoryId();
        cat1 = createdCategory1;

        ResponseEntity<Category> categoryResponse2;
        categoryResponse2 = api.createCategory(new Category("Hot C","Hot Coffee",shopIdForTest),shopIdForTest);
        Category createdCategory2 = categoryResponse2.getBody();
        //categories.add(createdCategory2);
        cateGoryIdFortest2 = createdCategory2.getCategoryId();
        cat2 = createdCategory2;

        api.logout();

    }

    private List<Category> preReqDataForItemTestWithCate(){

        api.login(XIPLI_ADMIN);

        ResponseEntity<List<Shop>> shopListResponse;
        ResponseEntity<Shop> shopResponse;
        List<Category> _categories = new ArrayList<Category>();

//        if (_categories == null) {
//            _categories = new HashSet<Category>();
//        }

        // test-case: create new shop and Category by POST
        Shop shop1 = testUtils.setupShopObject();
        shopResponse = api.createShop(shop1);
        Shop createdShop1 = shopResponse.getBody();
        shopIdForTest = createdShop1.getShopId();

        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        ResponseEntity<Category> categoryResponse1;
        categoryResponse1 = api.createCategory(new Category("Iced C","Iced Coffee",shopIdForTest),shopIdForTest);
        Category createdCategory1 = categoryResponse1.getBody();
        _categories.add(createdCategory1);
        cateGoryIdFortest1 = createdCategory1.getCategoryId();
        cat1 = createdCategory1;

        ResponseEntity<Category> categoryResponse2;
        categoryResponse2 = api.createCategory(new Category("Hot C","Hot Coffee",shopIdForTest),shopIdForTest);
        Category createdCategory2 = categoryResponse2.getBody();
        _categories.add(createdCategory2);
        cateGoryIdFortest2 = createdCategory2.getCategoryId();
        cat1 = createdCategory2;

        api.logout();

        return _categories;

    }
    private Item ItemCreateWithAssertion(){


        ResponseEntity<List<Item>> ItemListResponse;
        ResponseEntity<Item> ItemResponse;

        // CREATE AN ITEM UNDER ICED CATEGORY

        Item item = new Item("Latte","Late Coffe", BigDecimal.valueOf(5.00), shopIdForTest, cateGoryIdFortest1, null,Constants.StatusCodes.ACTIVE);
        ItemResponse = api.createItem(item,shopIdForTest);// createItemOption(itemOption1,shopIdForTest);
        assertEquals(HttpStatus.CREATED, ItemResponse.getStatusCode());
        Item createdItem = ItemResponse.getBody();

        // CREATE AN ITEM UNDER HOT CATEGORY

        Item item1 = new Item("Hot Latte","Hot Late Coffe", BigDecimal.valueOf(5.00), shopIdForTest, cateGoryIdFortest2, null,Constants.StatusCodes.ACTIVE);
        ItemResponse = api.createItem(item1,shopIdForTest);// createItemOption(itemOption1,shopIdForTest);
        assertEquals(HttpStatus.CREATED, ItemResponse.getStatusCode());
        Item createdItem1 = ItemResponse.getBody();

        assertNotNull(createdItem);
        assertTrue(createdItem.getItemId() > 0);
        item.setItemId (createdItem.getItemId());
        assertEquals(item.getItemId(), createdItem.getItemId());
        assertEquals(item.getName(), createdItem.getName());
        return createdItem;
    }
    private Item ItemCreateWithAssertion(List<Category> categories){


        ResponseEntity<List<Item>> ItemListResponse;
        ResponseEntity<Item> ItemResponse;

        //Set<Category> foo = new HashSet<Category>(categories);
        // CREATE AN ITEM UNDER ICED CATEGORY

        Item item = new Item("Latte","Late Coffe", BigDecimal.valueOf(5.00), shopIdForTest, cateGoryIdFortest1, null,Constants.StatusCodes.ACTIVE);
        item.setCategories(categories);
        ItemResponse = api.createItem(item,shopIdForTest);// createItemOption(itemOption1,shopIdForTest);
        assertEquals(HttpStatus.CREATED, ItemResponse.getStatusCode());
        Item createdItem = ItemResponse.getBody();

        // CREATE AN ITEM UNDER HOT CATEGORY

//        Item item1 = new Item("Hot Latte","Hot Late Coffe", BigDecimal.valueOf(5.00), shopIdForTest, cateGoryIdFortest2, null,Constants.StatusCodes.ACTIVE);
//        item1.setCategories(categories);
//        ItemResponse = api.createItem(item1,shopIdForTest);// createItemOption(itemOption1,shopIdForTest);
//        assertEquals(HttpStatus.CREATED, ItemResponse.getStatusCode());
//        Item createdItem1 = ItemResponse.getBody();

        assertNotNull(createdItem);
        assertTrue(createdItem.getItemId() > 0);
        item.setItemId (createdItem.getItemId());
        assertEquals(item.getItemId(), createdItem.getItemId());
        assertEquals(item.getName(), createdItem.getName());
        return createdItem;
    }
}
