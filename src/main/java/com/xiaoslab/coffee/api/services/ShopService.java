package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.repository.ShopRepository;
import com.xiaoslab.coffee.api.security.Roles;
import com.xiaoslab.coffee.api.specifications.ShopSpecifications;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return list(Optional.empty(), Optional.empty());
    }

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN, Roles.ROLE_X_ADMIN})
    public Shop get(long shopId) {
        Shop shop = shopRepository.findOne(shopId);
        if (shop == null || shop.getStatus() == Constants.StatusCodes.DELETED) {
            return null;
        } else {
            return shop;
        }
    }

    @Override
    @RolesAllowed(Roles.ROLE_X_ADMIN)
    public Shop create(Shop shop) {
        if (shop.getStatus() == null) {
            shop.setStatus(Constants.StatusCodes.INACTIVE);
        }
        return shopRepository.save(shop);
    }

    @Override
    @RolesAllowed(Roles.ROLE_X_ADMIN)
    public Shop update(Shop shop) {
        return  shopRepository.save(shop);
    }

    @Override
    @RolesAllowed(Roles.ROLE_X_ADMIN)
    public Shop delete(long shopid) {
        Shop shop = shopRepository.findOne(shopid);
        shop.setStatus(Constants.StatusCodes.DELETED);
        return shopRepository.save(shop);
    }

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN, Roles.ROLE_X_ADMIN})
    public List<Shop> list(Optional<Specification<Shop>> specOptional, Optional<Pageable> pageableOptional) {
        List<Shop> list = new ArrayList<>();

        Specification<Shop> specification;

        if (specOptional.isPresent()) {
            specification = Specifications.where(ShopSpecifications.notDeleted()).and(specOptional.get());
        } else {
            specification = Specifications.where(ShopSpecifications.notDeleted());
        }

        if (pageableOptional.isPresent()) {
            shopRepository.findAll(specification, pageableOptional.get()).forEach(list::add);
        } else {
            shopRepository.findAll(specification).forEach(list::add);
        }

        return list;
    }
}
