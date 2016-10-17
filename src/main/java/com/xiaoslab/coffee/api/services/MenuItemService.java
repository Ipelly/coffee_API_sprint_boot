package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.MenuItemDao;
import com.xiaoslab.coffee.api.objects.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.util.List;

public class MenuItemService {

    @Autowired
    private MenuItemDao menuItemDao;

    // access control and business logics will be implemented here at service level
    @RolesAllowed("USER")
    public List<MenuItem> listMenuItems() {
        return menuItemDao.listMenuItems();
    }

    public MenuItem create(MenuItem menuItem) {
        int id = menuItemDao.create(menuItem);
        return menuItemDao.get(id);
    }

}
