package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.ShopDao;
import com.xiaoslab.coffee.api.objects.Shop;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ShopService {

    @Autowired
    private ShopDao shopDao;

    // access control and business logics will be implemented here at service level
    // @RolesAllowed("USER")
    public List<Shop> listshops() {
        return shopDao.listshops();
    }

}
