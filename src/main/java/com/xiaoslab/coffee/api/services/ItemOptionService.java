package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.ItemOption;
import com.xiaoslab.coffee.api.repository.ItemOptionRepository;
import com.xiaoslab.coffee.api.repository.ItemRepository;
import com.xiaoslab.coffee.api.security.Roles;
import com.xiaoslab.coffee.api.specifications.ItemOptionSpecifications;
import com.xiaoslab.coffee.api.utility.BeanValidator;
import com.xiaoslab.coffee.api.utility.Constants;
import com.xiaoslab.coffee.api.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.annotation.security.RolesAllowed;
import java.util.*;

public class ItemOptionService implements IService<ItemOption> {

    @Autowired
    ItemOptionRepository itemOptionRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserUtility userUtility;

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN})
    public List<ItemOption> list(long itemId) {

        Specification<ItemOption> specification = Specifications
                .where(ItemOptionSpecifications.filterByItemId(itemId))
                .and(ItemOptionSpecifications.notDeleted());

        return itemOptionRepository.findAll(specification);
    }

    @Override
    @RolesAllowed({Roles.ROLE_SHOP_ADMIN})
    public List<ItemOption> updateAll(long itemId, List<ItemOption> itemOptions) {
        Item item = itemRepository.findOne(itemId);
        userUtility.checkUserCanManageShop(item.getShopId());

        Set<String> newOptionNames = new HashSet<>();
        for (ItemOption option : itemOptions) {
            option.setItemId(itemId);
            if (option.getStatus() == null) {
                option.setStatus(Constants.StatusCodes.INACTIVE);
            }
            BeanValidator.validate(option);
            newOptionNames.add(option.getName());
        }

        // delete options that do not exist in updated list
        List<ItemOption> existingOptions = list(itemId);
        for (ItemOption existingOption : existingOptions) {
            if (!newOptionNames.contains(existingOption.getName())) {
                existingOption.setStatus(Constants.StatusCodes.DELETED);
                itemOptionRepository.save(existingOption);
            }
        }

        itemOptionRepository.save(itemOptions);
        return list(itemId);
    }
}
