package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.repository.ItemRepository;
import com.xiaoslab.coffee.api.security.Roles;
import com.xiaoslab.coffee.api.specifications.ItemSpecifications;
import com.xiaoslab.coffee.api.utility.Constants;
import com.xiaoslab.coffee.api.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.annotation.security.RolesAllowed;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by ipeli on 10/16/16.
 */
public class ItemService implements IService<Item> {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserUtility userUtility;

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN, Roles.ROLE_X_ADMIN})
    public List<Item> list() {
        return itemRepository.findAll();
    }

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN, Roles.ROLE_X_ADMIN})
    public Item get(long itemId) {
        return itemRepository.getOne(itemId);
    }

    @Override
    @RolesAllowed(Roles.ROLE_SHOP_ADMIN)

    public Item create(Item item) {
        userUtility.checkUserCanAccessShop(item.getShopId());
        if (item.getStatus() == null) {
            item.setStatus(Constants.StatusCodes.INACTIVE);
        }
        return itemRepository.save(item);
    }

    @Override
    @RolesAllowed(Roles.ROLE_SHOP_ADMIN)
    public Item update(Item item) {
        return  itemRepository.save(item);
    }

    @Override
    @RolesAllowed({Roles.ROLE_SHOP_ADMIN, Roles.ROLE_X_ADMIN})
    public Item delete(long itemId) {
        Item item = itemRepository.findOne(itemId);
        item.setStatus(Constants.StatusCodes.DELETED);
        return itemRepository.save(item);
    }

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN, Roles.ROLE_X_ADMIN})
    public List<Item> list(Optional<Specification<Item>> specOptional, Optional<Pageable> pageableOptional) {
        List<Item> list = new ArrayList<>();

        Specification<Item> specification;

        if (specOptional.isPresent()) {
            specification = Specifications.where(ItemSpecifications.notDeleted()).and(specOptional.get());
        } else {
            specification = Specifications.where(ItemSpecifications.notDeleted());
        }
        if (pageableOptional.isPresent()) {
            itemRepository.findAll(specification, pageableOptional.get()).forEach(list::add);
        } else {
            itemRepository.findAll(specification).forEach(list::add);
        }
        return list;
    }

}
