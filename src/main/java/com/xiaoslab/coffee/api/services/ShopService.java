package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.ShopDao;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.*;

public class ShopService implements IService<Shop, String> {

    @Autowired
    private ShopDao shopDao;

    @Autowired
    ShopRepository shopRepository;

    // access control and business logics will be implemented here at service level
    // @RolesAllowed("USER")
    @Override
    public List<Shop> getAll() {
        return shopRepository.findAll().stream().filter(p -> p.isactive()  == true).collect(Collectors.toList());
    }

    @Override
    public Shop get(String shopId) {
       return (Shop) (shopRepository.findAll()
                .stream().filter(p -> p.getShopID() == Integer.parseInt(shopId)).collect(Collectors.toList())).get(0);
    }

    @Override
    public Shop Create(Shop shop) {
        shop.setIsactive(true);
        return  shopRepository.save(shop);
    }

    @Override
    public Shop Update(Shop shop) {
        return  shopRepository.save(shop);
    }

    @Override
    public Shop Delete(String shopid) {
        Shop shop = get(shopid);
        shop.setIsactive(false);
        return shopRepository.save(shop);
    }
}
