package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShopService implements IService<Shop> {

    @Autowired
    ShopRepository shopRepository;

    //Optional<Specification<Shop>> spec();
    //Optional<Pageable> pageable;

    // access control and business logics will be implemented here at service level
    // this function only return those records which status is active.
    // @RolesAllowed("USER")
    @Override
    public List<Shop> list() {
        return shopRepository.findAll().stream().filter(p -> p.getStatus()  == true).collect(Collectors.toList());
    }

    @Override
    public Shop get(long shopId) {
        return shopRepository.getOne(shopId);
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

    @Override
    public List<Shop> list(Optional<Specification<Shop>> spec, Optional<Pageable> pageable) {
        return null;
    }
}
