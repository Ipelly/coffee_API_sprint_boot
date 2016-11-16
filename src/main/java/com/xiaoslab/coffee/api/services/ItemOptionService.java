package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.ItemOption;
import com.xiaoslab.coffee.api.repository.ItemOptionRepository;
import com.xiaoslab.coffee.api.security.Roles;
import com.xiaoslab.coffee.api.specifications.ItemOptionSpecifications;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by ipeli on 11/15/16.
 */
public class ItemOptionService implements IService<ItemOption> {


    @Autowired
    ItemOptionRepository itemOptionRepository;

    @Override
    public List<ItemOption> list() {
        return list(Optional.empty(), Optional.empty());
    }

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN, Roles.ROLE_X_ADMIN})
    public List<ItemOption> list(Optional<Specification<ItemOption>> specOptional, Optional<Pageable> pageableOptional) {
        List<ItemOption> list = new ArrayList<>();

        Specification<ItemOption> specification;

        if (specOptional.isPresent()) {
            specification = Specifications.where(ItemOptionSpecifications.notDeleted()).and(specOptional.get());
        } else {
            specification = Specifications.where(ItemOptionSpecifications.notDeleted());
        }

        if (pageableOptional.isPresent()) {
            itemOptionRepository.findAll(specification, pageableOptional.get()).forEach(list::add);
        } else {
            itemOptionRepository.findAll(specification).forEach(list::add);
        }

        return list;
    }

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN, Roles.ROLE_X_ADMIN})
    public ItemOption get(long itemOptionid) {

        ItemOption itemOption = itemOptionRepository.getOne(itemOptionid);;
        if (itemOption == null || itemOption.getStatus() == Constants.StatusCodes.DELETED) {
            return null;
        } else {
            return itemOption;
        }
    }

    @Override
    @RolesAllowed({Roles.ROLE_SHOP_ADMIN, Roles.ROLE_X_ADMIN})
    public ItemOption create(ItemOption itemOption) {
        if (itemOption.getStatus() == null) {
            itemOption.setStatus(Constants.StatusCodes.ACTIVE);
        }
        return itemOptionRepository.save(itemOption);
    }

    @Override
    @RolesAllowed({Roles.ROLE_SHOP_ADMIN})
    public ItemOption update(ItemOption itemOption) {
        return  itemOptionRepository.save(itemOption);
    }

    @Override
    @RolesAllowed({Roles.ROLE_SHOP_ADMIN})
    public ItemOption delete(long itemOptionid) {
        ItemOption itemOption = itemOptionRepository.findOne(itemOptionid);
        itemOption.setStatus(Constants.StatusCodes.DELETED);
        return itemOptionRepository.save(itemOption);
    }
}
