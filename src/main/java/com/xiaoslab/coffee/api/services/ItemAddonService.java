package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.ItemAddon;
import com.xiaoslab.coffee.api.repository.ItemAddonRepository;
import com.xiaoslab.coffee.api.repository.ItemRepository;
import com.xiaoslab.coffee.api.security.Roles;
import com.xiaoslab.coffee.api.specifications.ItemAddonSpecifications;
import com.xiaoslab.coffee.api.utility.BeanValidator;
import com.xiaoslab.coffee.api.utility.Constants;
import com.xiaoslab.coffee.api.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.annotation.security.RolesAllowed;
import java.util.*;

public class ItemAddonService implements IService<ItemAddon> {

    @Autowired
    ItemAddonRepository itemAddonRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserUtility userUtility;

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN})
    public List<ItemAddon> list(long itemId) {

        Specification<ItemAddon> specification = Specifications
                .where(ItemAddonSpecifications.filterByItemId(itemId))
                .and(ItemAddonSpecifications.notDeleted());

        return itemAddonRepository.findAll(specification);
    }

    @Override
    @RolesAllowed({Roles.ROLE_SHOP_ADMIN})
    public List<ItemAddon> updateAll(long itemId, List<ItemAddon> itemAddons) {
        Item item = itemRepository.findOne(itemId);
        userUtility.checkUserCanManageShop(item.getShopId());

        Set<String> newAddonNames = new HashSet<>();
        for (ItemAddon addon : itemAddons) {
            addon.setItemId(itemId);
            if (addon.getStatus() == null) {
                addon.setStatus(Constants.StatusCodes.INACTIVE);
            }
            BeanValidator.validate(addon);
            newAddonNames.add(addon.getName());
        }

        // delete addons that do not exist in updated list
        List<ItemAddon> existingAddons = list(itemId);
        for (ItemAddon existingAddon : existingAddons) {
            if (!newAddonNames.contains(existingAddon.getName())) {
                existingAddon.setStatus(Constants.StatusCodes.DELETED);
                itemAddonRepository.save(existingAddon);
            }
        }

        itemAddonRepository.save(itemAddons);
        return list(itemId);
    }
}
