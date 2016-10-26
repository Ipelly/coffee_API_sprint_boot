package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.utilities.LoginUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShopTest {

    private long shopidfortest;
    Logger logger = Logger.getLogger(ShopTest.class);
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private IService<Shop> shopService;
    @Autowired
    private LoginUtils loginUtils;


    
    @Test
    public void checkDatabaseConnection() {
        Object verion = namedParameterJdbcTemplate.queryForList("SELECT VERSION()", new MapSqlParameterSource());
        logger.info(verion);
        assertNotNull(verion);
    }

    @Test
    public void createUpdateDeleteShop() {

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
        shop.setLongitute(new BigDecimal(-74.0623));
        shop.setRating(5);
        Shop createdUser = shopService.create(shop);
        shopidfortest = createdUser.getShopID();

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
        shop1.setLongitute(new BigDecimal(-74.0623));
        shop1.setRating(5);
        Shop createdSHhop1 = shopService.create(shop1);


        // test-case: Update new user
        createdSHhop1.setName("DD1 Edit");
        createdSHhop1.setAddress1("Midtown Edit");
        Shop createdUseredit = shopService.update(createdSHhop1);


        // test-case: Delete new user
        long shopIDForDelete = createdUseredit.getShopID();
        Shop deleteShopdel = shopService.delete(shopIDForDelete);

        List<Shop> list = shopService.getAll();
        assertEquals(1, list.size());
    }

    @Test
    public void getAllShop() {
        loginUtils.loginWithUserRole();
        List<Shop> items = shopService.getAll();
        logger.info(items);
        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test
    public void getAShop() {
        loginUtils.loginWithUserRole();
        Shop shop = shopService.get(shopidfortest);
        logger.info(shop);
        assertNotNull(shop);
        assertEquals("DD", shop.getName());
    }
}
