package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.ShopDao;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class ShopService implements IService<Shop> {

    @Autowired
    private ShopDao shopDao;

    @Autowired
    ShopRepository shopRepository;

    // access control and business logics will be implemented here at service level
    // this function only return those records which status is active.
    // @RolesAllowed("USER")
    @Override
    public List<Shop> getAll() {
        return shopRepository.findAll().stream().filter(p -> p.getStatus()  == true).collect(Collectors.toList());
    }

    @Override
    public Shop get(long shopId) {
        return shopRepository.getOne(shopId);
       //return (Shop) (shopRepository.findAll()
         //       .stream().filter(p -> p.getShopID() == Integer.parseInt(shopId)).collect(Collectors.toList())).get(0);
    }

    @Override
    public Shop create(Shop shop) {
        shop.setStatus(true);
        return shopRepository.save(shop);
    }

    @Override
    public Shop update(Shop shop) {
        return  shopRepository.save(shop);
    }

    @Override
    public Shop delete(long shopid) {
        Shop shop = shopRepository.findOne(shopid);
        shop.setStatus(false);
        return shopRepository.save(shop);
    }
}
