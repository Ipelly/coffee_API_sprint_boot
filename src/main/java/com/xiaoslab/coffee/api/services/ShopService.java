package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.ShopDao;
import com.xiaoslab.coffee.api.objects.Shop;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class ShopService implements IService<Shop> {

    @Autowired
    private ShopDao shopDao;

    // access control and business logics will be implemented here at service level
    // @RolesAllowed("USER")
    @Override
    public List<Shop> getAll() {
        return shopDao.Shops();
    }

    @Override
    public Shop get(String id) {
        return (Shop) (shopDao.Shops()
                .stream().filter(p -> p.getShopID() == Integer.parseInt(id)).collect(Collectors.toList())).get(0);
    }

    @Override
    public Boolean Insert(Shop obj) {
        return null;
    }

    @Override
    public Boolean Update(Shop pbj) {
        return null;
    }

    @Override
    public Boolean Delete(Shop obj) {
        return null;
    }
}
