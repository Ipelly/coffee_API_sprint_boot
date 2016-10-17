package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.MenuItem;
import com.xiaoslab.coffee.api.utilities.LoginUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SampleTest extends _BaseServiceTest {

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private LoginUtils loginUtils;

    @Test
    public void menuItemService(){
        loginUtils.loginWithUserRole();
        List<MenuItem> items =  menuItemService.listMenuItems();
        getLogger().info(items);
        assertNotNull(items);
        assertEquals(5, items.size());
    }

    @Test
    public void createMenuItem(){
        loginUtils.loginWithUserRole();
        
        MenuItem item = new MenuItem();
        item.setName("New coffee");
        item.setPrice(new BigDecimal(10));


        MenuItem createdItem = menuItemService.create(item);
        List<MenuItem> items =  menuItemService.listMenuItems();
        assertEquals(6, items.size());

    }
}
