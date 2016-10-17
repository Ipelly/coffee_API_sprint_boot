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
    private ShopService shopService;
    @Autowired
    private LoginUtils loginUtils;

    @Test
    public void checkDatabaseConnection() {
        Object verion = namedParameterJdbcTemplate.queryForList("SELECT VERSION()", new MapSqlParameterSource());
        logger.info(verion);
        assertNotNull(verion);
    }

    @Test
    public void shopService() {
        loginUtils.loginWithUserRole();
        List<Shop> items = shopService.listshops();
        logger.info(items);
        assertNotNull(items);
        assertEquals(0, items.size());
    }

}
