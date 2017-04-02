package com.xiaoslab.coffee.api.services;

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

public class ShopServiceTest extends _BaseServiceTest  {

    private long shopidfortest;

    Logger logger = Logger.getLogger(ShopServiceTest.class);

    @Autowired
    private IService<Shop> shopService;

    @Autowired
    private ServiceLoginUtils serviceLoginUtils;


    @Test
    public void createUpdateDeleteShop() {

        serviceLoginUtils.loginAsXAdmin();

        // test-case: create new shop 1
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

        // test-case: create new shop 2
        Shop shop1 = new Shop();
        shop1.setName("DD1");
        shop1.setAddress1("Midtown");
        shop1.setAddress2("14th Street");
        shop1.setCity("NYC");
        shop1.setState("NY");
        shop1.setZip("111.0");
        shop1.setPhone("6414517510");
        shop1.setLatitude(new BigDecimal(40.7426));
        shop1.setLongitude(new BigDecimal(-74.0623));
        shop1.setRating(5);
        shop1.setStatus(Constants.StatusCodes.ACTIVE);
        Shop createdSHhop1 = shopService.create(shop1);


        // test-case: Update new shop
        createdSHhop1.setName("DD1 Edit");
        createdSHhop1.setAddress1("Midtown Edit");
        Shop createdShopEdit = shopService.update(createdSHhop1);


        // test-case: Delete new shop
        long shopIDForDelete = createdShopEdit.getShopId();
        Shop deleteShopdel = shopService.delete(shopIDForDelete);

        List<Shop> list = shopService.list();
        assertEquals(1, list.size());
    }

    @Test
    public void getAllShop() {

        serviceLoginUtils.loginAsXAdmin();

        // test-case: create new shop 1
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

        serviceLoginUtils.loginAsCustomerUser();
        List<Shop> items = shopService.list();
        logger.info(items);
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void getShop() {

        serviceLoginUtils.loginAsXAdmin();
        // test-case: create new shop 1
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

        serviceLoginUtils.loginAsCustomerUser();
        Shop shopp = shopService.get(shopidfortest);
        logger.info(shopp);
        assertNotNull(shopp);
        assertEquals("DD", shop.getName());
    }
}
