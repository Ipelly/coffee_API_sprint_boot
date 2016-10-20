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

    Logger logger = Logger.getLogger(ShopTest.class);
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private IService<Shop,String> shopService;
    @Autowired
    private LoginUtils loginUtils;

    @Test
    public void checkDatabaseConnection() {
        Object verion = namedParameterJdbcTemplate.queryForList("SELECT VERSION()", new MapSqlParameterSource());
        logger.info(verion);
        assertNotNull(verion);
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
        Shop shop = shopService.get("2");
        logger.info(shop);
        assertNotNull(shop);
        assertEquals("DD", shop.getName());
    }

    @Test
    public void createUpdateDeleteShop() {

        // test-case: create new user
        Shop shop1 = new Shop();
        shop1.setName("StarBuks1");
        shop1.setAddress1("Midtown");
        shop1.setAddress2("14th Street");
        shop1.setCity("NYC");
        shop1.setState("NY");
        shop1.setZip("111.0");
        shop1.setPhone("6414517510");
        shop1.setLatitude(new BigDecimal(40.7426));
        shop1.setLongitute(new BigDecimal(-74.0623));
        shop1.setRating(5);
        Shop createdUser1 = shopService.Create(shop1);


        // test-case: Update new user
        createdUser1.setName("Starbuck Edit");
        createdUser1.setAddress1("Midtown Edit");
        Shop createdUseredit = shopService.Update(createdUser1);


        // test-case: Delete new user
        int shopIDForDelete = createdUseredit.getShopID();
        Shop deleteShopdel = shopService.Delete(Integer.toString(shopIDForDelete));

        List<Shop> list = shopService.getAll();
        assertEquals(2, list.size());
    }

}
