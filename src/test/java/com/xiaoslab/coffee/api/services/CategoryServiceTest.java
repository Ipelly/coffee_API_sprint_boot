package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Category;
import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.utilities.ServiceLoginUtils;
import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by islamma on 11/1/16.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class CategoryServiceTest extends _BaseServiceTest{

    private long categoryidfortest;
    private long shopidfortest;

    Logger logger = Logger.getLogger(CategoryServiceTest.class);

    @Autowired
    private IService<Category> categoryService;

    @Autowired
    private IService<Shop> shopService;

    @Autowired
    private ServiceLoginUtils serviceLoginUtils;

    @Test
    public void createUpdateDeleteCategory() {

        // test-case: create new Category under a shop
        createCategoryUnderShop();

        // test-case: Update category 2
        Category createdCategory = categoryService.get(categoryidfortest);
        createdCategory.setName("Iced Coffee");
        Category createdCategoryEdit = categoryService.update(createdCategory);

        // test-case: Delete new item
        Category deleteCategory = categoryService.delete(categoryidfortest);

        List<Category> categories = categoryService.list();
        assertEquals(1, categories.size());
   }

    @Test
    public void getAllItems() {

        createCategoryUnderShop();

        serviceLoginUtils.loginAsCustomerUser();
        List<Category> categorys = categoryService.list();
        logger.info(categorys);
        assertNotNull(categorys);
        assertEquals(2, categorys.size());
    }

    @Test
    public void getItem() {
        createCategoryUnderShop();

        serviceLoginUtils.loginAsCustomerUser();
        Category category = categoryService.get(categoryidfortest);
        logger.info(category);
        assertNotNull(category);
        assertEquals("Iced", category.getName());

    }


    private void createCategoryUnderShop(){
        Shop shop = createShop();
        serviceLoginUtils.loginAsShopAdmin(shop.getShopId());

        // Adding item1 to the shop
        serviceLoginUtils.loginAsShopAdmin(shopidfortest);
        Category category = new Category();
        category.setName("Iced");
        category.setDescription("Fresh brewed beans made with the milk of your choice");
        category.setShop_id (shopidfortest);
        category.setStatus(Constants.StatusCodes.ACTIVE);
        Category createdCategory = categoryService.create(category);
        categoryidfortest = createdCategory.getCategory_id();

        // Adding item2 to the shop
        Category category1 = new Category();
        category1.setName("Hot");
        category1.setDescription("Fresh brewed beans made with the milk of your choice 1");
        category1.setShop_id (shopidfortest);
        category1.setStatus(Constants.StatusCodes.ACTIVE);
        Category createdCategory1 = categoryService.create(category1);
    }
    private Shop createShop(){
        //test-case: create new shop and add item 1 to it
        serviceLoginUtils.loginAsXAdmin();
        Shop shop = new Shop();
        shop.setName("DD");
        shop.setAddress1("165 Liberty Ave");
        shop.setAddress2(", Jersey City");
        shop.setCity("JC");
        shop.setState("NJ");
        shop.setZip("07306");
        shop.setPhone("6414517510");
        shop.setLatitude(new BigDecimal(40.7426));
        shop.setLongitude(new BigDecimal(-74.0623));
        shop.setRating(5);
        shop.setStatus(Constants.StatusCodes.ACTIVE);
        Shop createdShop = shopService.create(shop);
        shopidfortest = createdShop.getShopId();
        return createdShop;
    }
}