package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.utilities.LoginUtils;
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

public class ItemServiceTest extends _BaseServiceTest{

    private long itemidfortest;
    private long shopidfortest;

    Logger logger = Logger.getLogger(ItemServiceTest.class);

    @Autowired
    private IService<Item> itemService;

    @Autowired
    private IService<Shop> shopService;

    @Autowired
    private LoginUtils loginUtils;

    @Test
    public void createUpdateDeleteItem() {

        // test-case: create new shop and add item 1 to it
        loginUtils.loginAsXAdmin();
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

        loginUtils.loginAsShopAdmin();
        // Adding item to the shop
        Item item = new Item();
        item.setName("latte");
        item.setDescription("Fresh brewed beans made with the milk of your choice");
        item.setPrice(new BigDecimal(3.2));
        item.setShopId(shopidfortest);
        item.setStatus(Constants.StatusCodes.ACTIVE);
        Item createdItem = itemService.create(item);
        itemidfortest = createdItem.getItemId();

        // test-case: adding item2
        Item item2 = new Item();
        item2.setName("Iced Venila Chai");
        item2.setDescription("Excellent mix of east and the west");
        item2.setPrice(new BigDecimal(5.49));
        item2.setShopId(shopidfortest);
        item2.setStatus(Constants.StatusCodes.INACTIVE);
        Item createdItem2 = itemService.create(item2);

        // test-case: Update item2
        createdItem2.setName("Hot Venila Chai");
        createdItem2.setPrice(new BigDecimal(5.19));
        Item createdItemEdit = itemService.update(createdItem2);

        // test-case: Delete new item
        long itemIDForDelete = createdItem.getItemId();
        Item deleteShopdel = itemService.delete(itemIDForDelete);

        List<Item> list = itemService.list();
        assertEquals(1, list.size());
   }

    @Test
    public void getAllItems() {

        // test-case: create new shop and add item 1 to it
        loginUtils.loginAsXAdmin();
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

        // Adding item1 to the shop
        loginUtils.loginAsShopAdmin();
        Item item = new Item();
        item.setName("latte");
        item.setDescription("Fresh brewed beans made with the milk of your choice");
        item.setPrice(new BigDecimal(3.2));
        item.setShopId(shopidfortest);
        item.setStatus(Constants.StatusCodes.ACTIVE);
        Item createdItem = itemService.create(item);
        itemidfortest = createdItem.getItemId();

        // Adding item2 to the shop
        Item item2 = new Item();
        item2.setName("Iced Venila Chai");
        item2.setDescription("Excellent mix of east and the west");
        item2.setPrice(new BigDecimal(5.49));
        item2.setShopId(shopidfortest);
        item2.setStatus(Constants.StatusCodes.INACTIVE);
        Item createdItem2 = itemService.create(item2);

        loginUtils.loginAsCustomerUser();
        List<Item> items = itemService.list();
        logger.info(items);
        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test
    public void getItem() {

        // test-case: create new shop and add item 1 to it
        loginUtils.loginAsXAdmin();
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

        // Adding item1 to the shop
        Item item = new Item();
        item.setName("latte");
        item.setDescription("Fresh brewed beans made with the milk of your choice");
        item.setPrice(new BigDecimal(3.2));
        item.setShopId(shopidfortest);
        item.setStatus(Constants.StatusCodes.ACTIVE);
        Item createdItem = itemService.create(item);
        itemidfortest = createdItem.getItemId();

        // Adding item2 to the shop
        Item item2 = new Item();
        item2.setName("Iced Venila Chai");
        item2.setDescription("Excellent mix of east and the west");
        item2.setPrice(new BigDecimal(5.49));
        item2.setShopId(shopidfortest);
        item2.setStatus(Constants.StatusCodes.INACTIVE);
        Item createdItem2 = itemService.create(item2);

        loginUtils.loginAsCustomerUser();
        Item itemm = itemService.get(itemidfortest);
        logger.info(itemm);
        assertNotNull(itemm);
        assertEquals("latte", item.getName());
    }
}