package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Category;
import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.repository.ItemRepository;
import com.xiaoslab.coffee.api.utilities.ServiceLoginUtils;
import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by islamma on 11/1/16.
 */

public class CategoryServiceTest extends _BaseServiceTest {

    private static Shop SHOP1;
    private static Shop SHOP2;
    private static Category SHOP1_CATEGORY1;
    private static Category SHOP1_CATEGORY2;
    private static Category SHOP1_CATEGORY3;
    private static Category SHOP2_CATEGORY1;
    private static Category SHOP2_CATEGORY2;

    @Before
    public void dataSetup() {
        serviceLoginUtils.loginAsXAdmin();
        SHOP1 = shopService.create(testUtils.setupShopObject());
        SHOP2 = shopService.create(testUtils.setupShopObject());

        serviceLoginUtils.loginAsShopAdmin(SHOP1.getShopId());
        SHOP1_CATEGORY1 = categoryService.create(new Category("Iced Drinks", "Iced Drinks for Shop1", SHOP1.getShopId()));
        SHOP1_CATEGORY2 = categoryService.create(new Category("Hot Drinks", "Hot Drinks for Shop1", SHOP1.getShopId()));
        SHOP1_CATEGORY3 = categoryService.create(new Category("Smoothies", "Smoothies for Shop1", SHOP1.getShopId()));

        serviceLoginUtils.loginAsShopAdmin(SHOP2.getShopId());
        SHOP2_CATEGORY1 = categoryService.create(new Category("Iced Drinks", "Iced Drinks for Shop2", SHOP2.getShopId()));
        SHOP2_CATEGORY2 = categoryService.create(new Category("Hot Drinks", "Hot Drinks for Shop2", SHOP2.getShopId()));
    }

    @Test
    public void addRemoveItemsFromCategory() {
        // Not needed. Already covered in ItemServiceTest
    }

    @Test
    public void createCategory() {
        // TODO
    }

    @Test
    public void getCategory() {
        // TODO
    }

    @Test
    public void updateCategory() {
        // TODO
    }

    @Test
    public void deleteCategory() {
        // TODO
    }

    @Test
    public void listCategory() {
        // TODO
    }

    // TODO: more tests
}