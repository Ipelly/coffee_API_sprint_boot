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
 * Created by ipeli on 10/19/16.
 */
public class CategoryAPITest extends _BaseAPITest {

    long shopIdForTest,categoryIdFortest;

    @Test
    public void createAndGetAndListOfCategory() throws Exception {

        ResponseEntity<List<Category>> categoryListResponse;
        ResponseEntity<Category> categoryResponse;

        preReqDataForCetagoryTest();


        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        Category createCategory = categoryCreateWithAssertion();

        api.login(CUSTOMER_USER);

        categoryListResponse = api.listCategory(shopIdForTest);
        assertEquals(HttpStatus.OK, categoryListResponse.getStatusCode());
        List<Category> categoryList = categoryListResponse.getBody();
        assertEquals(1, categoryList.size());
        assertEquals(createCategory, categoryList.get(0));
    }

    @Test
    public void createCategoryWithoutAuthorization() throws Exception {

        preReqDataForCetagoryTest();

        ResponseEntity<Category> categoryResponse;

        Category category = new Category("Iced","Iced coffee",shopIdForTest);
        categoryResponse = api.createCategory(category,shopIdForTest);// createItemOption(itemOption1,shopIdForTest);
        assertEquals(HttpStatus.UNAUTHORIZED, categoryResponse.getStatusCode());

        api.login(CUSTOMER_USER);
        categoryResponse = api.createCategory(category,shopIdForTest);// createItemOption(itemOption1,shopIdForTest);
        assertEquals(HttpStatus.FORBIDDEN, categoryResponse.getStatusCode());

    }

    @Test
    public void updateCategorynWithoutAuthorization() throws Exception {

        ResponseEntity<Category> categoryResponse;
        preReqDataForCetagoryTest();


        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        Category createdCategory = categoryCreateWithAssertion();

        api.logout();


        createdCategory.setName("Iced Coffee");
        categoryResponse = api.updateCategory(createdCategory,shopIdForTest,createdCategory.getCategory_id());
        assertEquals(HttpStatus.UNAUTHORIZED, categoryResponse.getStatusCode());

        api.login(CUSTOMER_USER);
        categoryResponse = api.updateCategory(createdCategory,shopIdForTest,createdCategory.getCategory_id());
        assertEquals(HttpStatus.FORBIDDEN, categoryResponse.getStatusCode());

        api.login(XIPLI_ADMIN);
        categoryResponse = api.updateCategory(createdCategory,shopIdForTest,createdCategory.getCategory_id());
        assertEquals(HttpStatus.FORBIDDEN, categoryResponse.getStatusCode());

    }

    @Test
    public void deleteItemOptionWithoutAuthorization() throws Exception {

        ResponseEntity<Category> categoryResponse;

        preReqDataForCetagoryTest();


        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        Category createdCategory = categoryCreateWithAssertion();

        api.logout();

        categoryResponse = api.deleteCategory(createdCategory,shopIdForTest,createdCategory.getCategory_id());
        assertEquals(HttpStatus.UNAUTHORIZED, categoryResponse.getStatusCode());

        api.login(CUSTOMER_USER);
        categoryResponse = api.deleteCategory(createdCategory,shopIdForTest,createdCategory.getCategory_id());
        assertEquals(HttpStatus.FORBIDDEN, categoryResponse.getStatusCode());

        api.login(XIPLI_ADMIN);
        categoryResponse = api.deleteCategory(createdCategory,shopIdForTest,createdCategory.getCategory_id());
        assertEquals(HttpStatus.FORBIDDEN, categoryResponse.getStatusCode());

    }

    @Test
    public void createAndUpdateAndDeleteCategory() throws Exception {

        ResponseEntity<Category> categoryResponse;

        preReqDataForCetagoryTest();


        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        Category createdCategory = categoryCreateWithAssertion();


        createdCategory.setName("Iced Coffee");
        categoryResponse = api.updateCategory(createdCategory,shopIdForTest,createdCategory.getCategory_id());
        assertEquals(HttpStatus.OK, categoryResponse.getStatusCode());
        Category updatedCategory = categoryResponse.getBody();
        assertNotNull(updatedCategory);
        assertEquals(createdCategory, updatedCategory);
        assertThat(updatedCategory.getName(), is(equalTo("Iced Coffee")));

        categoryResponse = api.deleteCategory(createdCategory,shopIdForTest,createdCategory.getCategory_id());

        categoryResponse = api.getCategory(shopIdForTest,createdCategory.getCategory_id ());
        assertEquals(HttpStatus.NOT_FOUND, categoryResponse.getStatusCode());
    }

    @Test
    public void deleteCategoryWithItemList() throws Exception {

        ResponseEntity<List<Category>> categoryListResponse;
        ResponseEntity<Category> categoryResponse;

        preReqDataForCetagoryTest();


        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        Category createdCategory = categoryCreateWithAssertion();

        ResponseEntity<List<Item>> ItemListResponse;
        ResponseEntity<Item> ItemResponse;

        Item item = new Item("Latte","Late Coffe", BigDecimal.valueOf(5.00), shopIdForTest, createdCategory.getCategory_id(), Constants.StatusCodes.ACTIVE);

        ItemResponse = api.createItem(item,shopIdForTest);// createItemOption(itemOption1,shopIdForTest);
        assertEquals(HttpStatus.CREATED, ItemResponse.getStatusCode());

        categoryResponse = api.deleteCategory(createdCategory,shopIdForTest,createdCategory.getCategory_id());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, categoryResponse.getStatusCode());
    }

    @Test
    public void notFoundCategoryId() throws Exception {

        api.login(XIPLI_ADMIN);

        ResponseEntity<Category> response;

        // test-case: GET
        response = api.getCategory(shopIdForTest,Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // test-case: PUT
        response = api.updateCategory(new Category(),shopIdForTest,Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // test-case: DELETE
        response = api.deleteCategory(new Category(),shopIdForTest,Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private void preReqDataForCetagoryTest(){

        api.login(XIPLI_ADMIN);

        ResponseEntity<List<Shop>> listResponse;
        ResponseEntity<Shop> response;
        // test-case: create new shop by POST
        Shop shop1 = testUtils.setupShopObject();
        response = api.createShop(shop1);
        Shop createdShop1 = response.getBody();
        shopIdForTest = createdShop1.getShopId();

        api.logout();

    }
    private Category categoryCreateWithAssertion(){
        ResponseEntity<List<Category>> categoryListResponse;
        ResponseEntity<Category> categoryResponse;
        Category category = new Category("Iced","Iced coffee",shopIdForTest);

        categoryResponse = api.createCategory(category,shopIdForTest);// createItemOption(itemOption1,shopIdForTest);
        assertEquals(HttpStatus.CREATED, categoryResponse.getStatusCode());
        Category createCategory = categoryResponse.getBody();
        assertNotNull(createCategory);
        assertTrue(createCategory.getCategory_id() > 0);
        category.setCategory_id (createCategory.getCategory_id());
        assertEquals(category, createCategory);
        return createCategory;
    }
}
