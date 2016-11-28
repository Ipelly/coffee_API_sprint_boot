package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Addon;
import com.xiaoslab.coffee.api.repository.AddonRepository;
import com.xiaoslab.coffee.api.security.Roles;
import com.xiaoslab.coffee.api.specifications.AddonSpecifications;
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
public class AddonService implements IService<Addon> {

    @Autowired
    AddonRepository addonRepository;

    @Autowired
    UserUtility userUtility;

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN})
    public List<Addon> list() {
        return list(Optional.empty(), Optional.empty());
    }

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN})
    public Addon get(long addonId) {
        Addon addon = addonRepository.getOne(addonId);
        if (addon == null || addon.getStatus() == Constants.StatusCodes.DELETED) {
            return null;
        } else {
            return addon;
        }
    }

    @Override
    @RolesAllowed(Roles.ROLE_SHOP_ADMIN)
    public Addon create(Addon addon) {
        userUtility.checkUserCanAccessShop(addon.getShopId ());
        if (addon.getStatus() == null) {
            addon.setStatus(Constants.StatusCodes.INACTIVE);
        }
        return addonRepository.save(addon);
    }

    @Override
    @RolesAllowed(Roles.ROLE_SHOP_ADMIN)
    public Addon update(Addon addon) {
        userUtility.checkUserCanAccessShop(addon.getShopId ());
        return  addonRepository.save(addon);
    }

    @Override
    @RolesAllowed({Roles.ROLE_SHOP_ADMIN})
    public Addon delete(long addonId) {
        Addon addon = addonRepository.findOne(addonId);
        userUtility.checkUserCanAccessShop(addon.getShopId ());
        addon.setStatus(Constants.StatusCodes.DELETED);
        return addonRepository.save(addon);
    }

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN})
    public List<Addon> list(Optional<Specification<Addon>> specOptional, Optional<Pageable> pageableOptional) {
        List<Addon> list = new ArrayList<>();

        Specification<Addon> specification;

        if (specOptional.isPresent()) {
            specification = Specifications.where(AddonSpecifications.notDeleted()).and(specOptional.get());
        } else {
            specification = Specifications.where(AddonSpecifications.notDeleted());
        }
        if (pageableOptional.isPresent()) {
            addonRepository.findAll(specification, pageableOptional.get()).forEach(list::add);
        } else {
            addonRepository.findAll(specification).forEach(list::add);
        }
        return list;
    }
}
