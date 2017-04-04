package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.*;
import com.xiaoslab.coffee.api.utility.Constants;
import org.junit.Before;
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

    private long shopIdForTest, categoryIdForTest1, categoryIdForTest2;
    private Shop shop1;
    private User shop1Admin;
    private Category shop1Category1;
    private Category shop1Category2;

    @Before
    public void dataSetup(){

        api.login(XIPLI_ADMIN);

        ResponseEntity<List<Shop>> shopListResponse;
        ResponseEntity<Shop> shopResponse;
        //List<Category> categories = new ArrayList<Category>();

        // test-case: create new shop and Category by POST
        Shop shop1 = testUtils.setupShopObject();
        shopResponse = api.createShop(shop1);
        Shop createdShop1 = shopResponse.getBody();
        shopIdForTest = createdShop1.getShopId();
        this.shop1 = createdShop1;

        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        shop1Admin = shopAdmin;
        api.login(shopAdmin);

        ResponseEntity<Category> categoryResponse1;
        categoryResponse1 = api.createCategory(shopIdForTest, new Category("Iced C","Iced Coffee",shopIdForTest));
        Category createdCategory1 = categoryResponse1.getBody();
        //categories.add(createdCategory1);
        categoryIdForTest1 = createdCategory1.getCategoryId();
        shop1Category1 = createdCategory1;

        ResponseEntity<Category> categoryResponse2;
        categoryResponse2 = api.createCategory(shopIdForTest, new Category("Hot C","Hot Coffee",shopIdForTest));
        Category createdCategory2 = categoryResponse2.getBody();
        //categories.add(createdCategory2);
        categoryIdForTest2 = createdCategory2.getCategoryId();
        shop1Category2 = createdCategory2;

        api.logout();

    }

    @Test
    public void createUpdateDeleteItemCheckCategory() throws Exception {

        ResponseEntity<Item> itemResponse;
        ResponseEntity<Category> categoryResponse;

        api.login(shop1Admin);

        // create 1 item
        Item item1 = testUtils.setupItemObjectForShop(shop1.getShopId());
        item1.setCategoryIds(new HashSet<>(Arrays.asList(categoryIdForTest2)));
        itemResponse = api.createItem(shop1.getShopId(), item1);
        item1 = itemResponse.getBody();
        assertEquals(new HashSet<>(Arrays.asList(categoryIdForTest2)), itemResponse.getBody().getCategoryIds());

        itemResponse = api.getItem(shop1.getShopId(), item1.getItemId());
        assertEquals(HttpStatus.OK, itemResponse.getStatusCode());
        assertEquals(new HashSet<>(Arrays.asList(categoryIdForTest2)), itemResponse.getBody().getCategoryIds());

        categoryResponse = api.getCategory(shop1.getShopId(), categoryIdForTest1);
        categoryResponse = api.getCategory(shop1.getShopId(), categoryIdForTest2);

        // create 2 item
        Item item2 = testUtils.setupItemObjectForShop(shop1.getShopId());
        item2.setCategoryIds(new HashSet<>(Arrays.asList(categoryIdForTest1, categoryIdForTest2)));
        itemResponse = api.createItem(shop1.getShopId(), item2);
        item2 = itemResponse.getBody();
        assertEquals(new HashSet<>(Arrays.asList(categoryIdForTest1, categoryIdForTest2)), itemResponse.getBody().getCategoryIds());

        itemResponse = api.getItem(shop1.getShopId(), item2.getItemId());
        assertEquals(HttpStatus.OK, itemResponse.getStatusCode());
        assertEquals(new HashSet<>(Arrays.asList(categoryIdForTest1, categoryIdForTest2)), itemResponse.getBody().getCategoryIds());

        categoryResponse = api.getCategory(shop1.getShopId(), categoryIdForTest1);
        assertEquals(HttpStatus.OK, categoryResponse.getStatusCode());
        assertEquals(new HashSet<>(Arrays.asList(item2.getItemId())), categoryResponse.getBody().getItemIds());

        categoryResponse = api.getCategory(shop1.getShopId(), categoryIdForTest2);
        assertEquals(HttpStatus.OK, categoryResponse.getStatusCode());
        assertEquals(new HashSet<>(Arrays.asList(item1.getItemId(), item2.getItemId())), categoryResponse.getBody().getItemIds());

        // update category - check item
        categoryResponse = api.getCategory(shop1.getShopId(), categoryIdForTest1);
        Category categoryToUpdate = categoryResponse.getBody();
        categoryToUpdate.setItemIds(new HashSet<>(Arrays.asList(item1.getItemId())));
        categoryResponse = api.updateCategory(shop1.getShopId(), categoryToUpdate.getCategoryId(), categoryToUpdate);

        categoryResponse = api.getCategory(shop1.getShopId(), categoryIdForTest1);
        assertEquals(HttpStatus.OK, categoryResponse.getStatusCode());
        assertEquals(new HashSet<>(Arrays.asList(item1.getItemId())), categoryResponse.getBody().getItemIds());

        categoryResponse = api.getCategory(shop1.getShopId(), categoryIdForTest2);
        assertEquals(HttpStatus.OK, categoryResponse.getStatusCode());
        assertEquals(new HashSet<>(Arrays.asList(item1.getItemId(), item2.getItemId())), categoryResponse.getBody().getItemIds());

        // delete category - check item
        categoryResponse = api.deleteCategory(shop1.getShopId(), categoryIdForTest2);
        assertEquals(HttpStatus.NO_CONTENT, categoryResponse.getStatusCode());

        itemResponse = api.getItem(shop1.getShopId(), item1.getItemId());
        assertEquals(HttpStatus.OK, itemResponse.getStatusCode());
        assertEquals(new HashSet<>(Arrays.asList(categoryIdForTest1)), itemResponse.getBody().getCategoryIds());

        itemResponse = api.getItem(shop1.getShopId(), item2.getItemId());
        assertEquals(HttpStatus.OK, itemResponse.getStatusCode());
        assertEquals(new HashSet<>(Arrays.asList()), itemResponse.getBody().getCategoryIds());

    }

    @Test
    public void createAndGetAndListOfItemForFirstCategory() throws Exception {

        ResponseEntity<List<Item>> itemListResponse;
        ResponseEntity<Item> itemResponse;

        List<Category> cats = preReqDataForItemTestWithCate();

        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        Item createItem = itemCreateWithAssertion(cats);

        api.login(CUSTOMER_USER);

        itemListResponse = api.listItemForCategory(shopIdForTest, categoryIdForTest1);
        assertEquals(HttpStatus.OK, itemListResponse.getStatusCode());
        List<Item> ItemList = itemListResponse.getBody();
        assertEquals(1, ItemList.size());
        assertEquals("Latte", ItemList.get(0).getName());
    }

    @Test
    public void createAndGetAndListOfItemFor2ndCategory() throws Exception {

        ResponseEntity<List<Item>> itemListResponse;
        ResponseEntity<Item> itemResponse;

        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        //Item createItem = itemCreateWithAssertion();
        Item createItem =  itemCreateWithAssertion(Arrays.asList(shop1Category2));
        api.login(CUSTOMER_USER);

        itemListResponse = api.listItemForCategory(shopIdForTest, categoryIdForTest2);
        assertEquals(HttpStatus.OK, itemListResponse.getStatusCode());
        List<Item> ItemList2 = itemListResponse.getBody();
        assertEquals(1, ItemList2.size());
        assertEquals("Latte", ItemList2.get(0).getName());
    }

    @Test
    public void createAndGetAndListOfItemForShop() throws Exception {

        ResponseEntity<List<Item>> itemListResponse;
        ResponseEntity<Item> itemResponse;

        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        Item createItem = itemCreateWithAssertion();

        api.login(CUSTOMER_USER);

        itemListResponse = api.listItem(shopIdForTest);
        assertEquals(HttpStatus.OK, itemListResponse.getStatusCode());
        List<Item> ItemList = itemListResponse.getBody();
        assertEquals(2, ItemList.size());
    }

    @Test
    public void createItemWithoutAuthorization() throws Exception {

        ResponseEntity<Item> ItemResponse;

        Item item = new Item("Latte","Late Coffe", BigDecimal.valueOf(5.00), shopIdForTest, Constants.StatusCodes.ACTIVE);

        ItemResponse = api.createItem(shopIdForTest, item);
        assertEquals(HttpStatus.UNAUTHORIZED, ItemResponse.getStatusCode());

        api.login(CUSTOMER_USER);
        ItemResponse = api.createItem(shopIdForTest, item);
        assertEquals(HttpStatus.FORBIDDEN, ItemResponse.getStatusCode());

    }

    @Test
    public void updateItemWithoutAuthorization() throws Exception {

        ResponseEntity<Item> itemResponse;

        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        Item createdItem = itemCreateWithAssertion();

        api.logout();

        createdItem.setName("Iced Coffee");
        itemResponse = api.updateItem(shopIdForTest, createdItem.getItemId(), createdItem);
        assertEquals(HttpStatus.UNAUTHORIZED, itemResponse.getStatusCode());

        api.login(CUSTOMER_USER);
        itemResponse = api.updateItem(shopIdForTest, createdItem.getItemId(), createdItem);
        assertEquals(HttpStatus.FORBIDDEN, itemResponse.getStatusCode());

        api.login(XIPLI_ADMIN);
        itemResponse = api.updateItem(shopIdForTest, createdItem.getItemId(), createdItem);
        assertEquals(HttpStatus.FORBIDDEN, itemResponse.getStatusCode());

    }

    @Test
    public void deleteItemOptionWithoutAuthorization() throws Exception {

        ResponseEntity<Item> itemResponse;

        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        Item createdItem = itemCreateWithAssertion();

        api.logout();

        itemResponse = api.deleteItem(shopIdForTest, createdItem.getItemId());
        assertEquals(HttpStatus.UNAUTHORIZED, itemResponse.getStatusCode());

        api.login(CUSTOMER_USER);
        itemResponse = api.deleteItem(shopIdForTest, createdItem.getItemId());
        assertEquals(HttpStatus.FORBIDDEN, itemResponse.getStatusCode());

        api.login(XIPLI_ADMIN);
        itemResponse = api.deleteItem(shopIdForTest, createdItem.getItemId());
        assertEquals(HttpStatus.FORBIDDEN, itemResponse.getStatusCode());

    }

    @Test
    public void createAndUpdateAndDeleteItem() throws Exception {

        ResponseEntity<Item> ItemResponse;

        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        Item createdItem = itemCreateWithAssertion();


        createdItem.setName("Iced Coffee");
        ItemResponse = api.updateItem(shopIdForTest, createdItem.getItemId(), createdItem);
        assertEquals(HttpStatus.OK, ItemResponse.getStatusCode());
        Item updatedItem = ItemResponse.getBody();
        assertNotNull(updatedItem);
        assertEquals(createdItem, updatedItem);
        assertThat(updatedItem.getName(), is(equalTo("Iced Coffee")));

        ItemResponse = api.deleteItem(shopIdForTest, createdItem.getItemId());

        ItemResponse = api.getItem(createdItem.getShopId(), createdItem.getItemId());
        assertEquals(HttpStatus.NOT_FOUND, ItemResponse.getStatusCode());
    }

    @Test
    public void notFoundItemId() throws Exception {

        api.login(XIPLI_ADMIN);

        ResponseEntity<Item> response;

        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        Item item = new Item("","", BigDecimal.valueOf(5.00), shopIdForTest, Constants.StatusCodes.ACTIVE);
        // test-case: GET
        response = api.getItem(shopIdForTest, Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // test-case: PUT
        response = api.updateItem(shopIdForTest, Integer.MAX_VALUE, item);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // test-case: DELETE
        response = api.deleteItem(shopIdForTest, Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
        categoryResponse1 = api.createCategory(shopIdForTest, new Category("Iced C","Iced Coffee",shopIdForTest));
        Category createdCategory1 = categoryResponse1.getBody();
        _categories.add(createdCategory1);
        categoryIdForTest1 = createdCategory1.getCategoryId();
        shop1Category1 = createdCategory1;

        ResponseEntity<Category> categoryResponse2;
        categoryResponse2 = api.createCategory(shopIdForTest, new Category("Hot C","Hot Coffee",shopIdForTest));
        Category createdCategory2 = categoryResponse2.getBody();
        _categories.add(createdCategory2);
        categoryIdForTest2 = createdCategory2.getCategoryId();
        shop1Category1 = createdCategory2;

        api.logout();

        return _categories;

    }
    private Item itemCreateWithAssertion(){


        ResponseEntity<List<Item>> ItemListResponse;
        ResponseEntity<Item> ItemResponse;

        // CREATE AN ITEM UNDER ICED CATEGORY

        Item item = new Item("Latte","Late Coffe", BigDecimal.valueOf(5.00), shopIdForTest,Constants.StatusCodes.ACTIVE);
        ItemResponse = api.createItem(shopIdForTest, item);// createItemOption(itemOption1,shopIdForTest);
        assertEquals(HttpStatus.CREATED, ItemResponse.getStatusCode());
        Item createdItem = ItemResponse.getBody();

        // CREATE AN ITEM UNDER HOT CATEGORY

        Item item1 = new Item("Hot Latte","Hot Late Coffe", BigDecimal.valueOf(5.00), shopIdForTest,Constants.StatusCodes.ACTIVE);
        ItemResponse = api.createItem(shopIdForTest, item1);// createItemOption(itemOption1,shopIdForTest);
        assertEquals(HttpStatus.CREATED, ItemResponse.getStatusCode());
        Item createdItem1 = ItemResponse.getBody();

        assertNotNull(createdItem);
        assertTrue(createdItem.getItemId() > 0);
        item.setItemId (createdItem.getItemId());
        assertEquals(item.getItemId(), createdItem.getItemId());
        assertEquals(item.getName(), createdItem.getName());
        return createdItem;
    }

    private Item itemCreateWithAssertion(List<Category> categories){

        ResponseEntity<List<Item>> ItemListResponse;
        ResponseEntity<Item> ItemResponse;

        //Set<Category> foo = new HashSet<Category>(categories);
        // CREATE AN ITEM UNDER ICED CATEGORY

        Item item = new Item("Latte","Late Coffe", BigDecimal.valueOf(5.00), shopIdForTest, Constants.StatusCodes.ACTIVE);
        Set<Long> categoryIds = new HashSet<>();
        for (Category category : categories) {
            categoryIds.add(category.getCategoryId());
        }
        item.setCategoryIds(categoryIds);
        ItemResponse = api.createItem(shopIdForTest, item);// createItemOption(itemOption1,shopIdForTest);
        assertEquals(HttpStatus.CREATED, ItemResponse.getStatusCode());
        Item createdItem = ItemResponse.getBody();

        // CREATE AN ITEM UNDER HOT CATEGORY

//        Item item1 = new Item("Hot Latte","Hot Late Coffe", BigDecimal.valueOf(5.00), shopIdForTest, categoryIdForTest2, null,Constants.StatusCodes.ACTIVE);
//        item1.setCategoryIds(categories);
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
