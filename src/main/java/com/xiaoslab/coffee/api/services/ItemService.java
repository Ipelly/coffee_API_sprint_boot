package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Category;
import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.repository.CategoryRepository;
import com.xiaoslab.coffee.api.repository.ItemRepository;
import com.xiaoslab.coffee.api.security.Roles;
import com.xiaoslab.coffee.api.specifications.ItemSpecifications;
import com.xiaoslab.coffee.api.utility.BeanValidator;
import com.xiaoslab.coffee.api.utility.Constants;
import com.xiaoslab.coffee.api.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.util.CollectionUtils;

import javax.annotation.security.RolesAllowed;
import java.util.*;

/**
 * Created by ipeli on 10/02/17.
 */
public class ItemService implements IService<Item> {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    IService<Category> categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserUtility userUtility;

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN})
    public List<Item> list() {
        return list(Optional.empty(), Optional.empty());
    }

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN})
    public Item get(long itemId) {
        Item item = itemRepository.findOne(itemId);
        if (item == null || item.getStatus() == Constants.StatusCodes.DELETED) {
            return null;
        } else if (userUtility.isShopUser() && userUtility.getUserShopId() != item.getShopId()) {
            return null;
        } else {
            return item;
        }
    }

    @Override
    @RolesAllowed(Roles.ROLE_SHOP_ADMIN)
    public Item create(Item item) {
        userUtility.checkUserCanManageShop(item.getShopId());
        if (item.getStatus() == null) {
            item.setStatus(Constants.StatusCodes.INACTIVE);
        }
        if (item.getCategoryIds() == null) {
            item.setCategoryIds(new HashSet<>());
        }
        BeanValidator.validate(item);
        checkCategoryOwnership(item.getCategoryIds());
        return itemRepository.save(item);
    }

    @Override
    @RolesAllowed(Roles.ROLE_SHOP_ADMIN)
    public Item update(Item item) {
        userUtility.checkUserCanManageShop(item.getShopId());
        BeanValidator.validate(item);
        checkCategoryOwnership(item.getCategoryIds());
        return itemRepository.save(item);
    }

    @Override
    @RolesAllowed({Roles.ROLE_SHOP_ADMIN})
    public Item delete(long ItemId) {
        Item item = itemRepository.findOne(ItemId);
        userUtility.checkUserCanManageShop(item.getShopId());
        item.setStatus(Constants.StatusCodes.DELETED);
        item.setCategoryIds(new HashSet<>());
        return itemRepository.save(item);
    }

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN})
    public List<Item> list(Optional<Specification<Item>> specOptional, Optional<Pageable> pageableOptional) {
        List<Item> list = new ArrayList<>();

        Specification<Item> specification;

        if (specOptional.isPresent()) {
            specification = Specifications.where(ItemSpecifications.notDeleted()).and(specOptional.get());
        } else {
            specification = Specifications.where(ItemSpecifications.notDeleted());
        }

        if (userUtility.isShopUser()) {
            specification = Specifications.where(specification).and(ItemSpecifications.itemListForShop(userUtility.getUserShopId()));
        }

        if (pageableOptional.isPresent()) {
            itemRepository.findAll(specification, pageableOptional.get()).forEach(list::add);
        } else {
            itemRepository.findAll(specification).forEach(list::add);
        }

        return list;
    }

    private void checkCategoryOwnership(Set<Long> categoryIds) {
        for (long categoryId : categoryIds) {
            Category category = categoryService.get(categoryId);
            if (category == null || category.getShopId() != userUtility.getUserShopId()) {
                throw new IllegalArgumentException(categoryId + " is not a valid categoryId");
            }
        }
    }
}
