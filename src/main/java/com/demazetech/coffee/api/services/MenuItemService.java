package com.demazetech.coffee.api.services;

import com.demazetech.coffee.api.dao.MenuItemDao;
import com.demazetech.coffee.api.objects.MenuItem;
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

}
