package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.MenuItem;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private LoginUtils loginUtils;

    Logger logger = Logger.getLogger(SampleTest.class);

    @Test
    public void checkDatabaseConnection(){
        Object verion = namedParameterJdbcTemplate.queryForList("SELECT VERSION()", new MapSqlParameterSource());
        logger.info(verion);
        assertNotNull(verion);
    }

    @Test
    public void menuItemService(){
        loginUtils.loginWithUserRole();
        List<MenuItem> items =  menuItemService.listMenuItems();
        logger.info(items);
        assertNotNull(items);
        assertEquals(5, items.size());
    }

}
