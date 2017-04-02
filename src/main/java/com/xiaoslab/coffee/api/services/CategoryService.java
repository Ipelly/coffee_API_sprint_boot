package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Category;
import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.repository.CategoryRepository;
import com.xiaoslab.coffee.api.repository.ItemRepository;
import com.xiaoslab.coffee.api.security.Roles;
import com.xiaoslab.coffee.api.specifications.CategorySpecifications;
import com.xiaoslab.coffee.api.specifications.ItemSpecifications;
import com.xiaoslab.coffee.api.utility.BeanValidator;
import com.xiaoslab.coffee.api.utility.CategoryUtility;
import com.xiaoslab.coffee.api.utility.Constants;
import com.xiaoslab.coffee.api.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import java.util.*;

/**
 * Created by ipeli on 10/16/16.
 */
@Service
public class CategoryService implements IService<Category> {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    IService<Item> itemService;

    @Autowired
    UserUtility userUtility;

    @Autowired
    CategoryUtility categoryUtility;

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN, Roles.ROLE_X_ADMIN})
    public List<Category> list() {
        return list(Optional.empty(), Optional.empty());
    }

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN, Roles.ROLE_X_ADMIN})
    public Category get(long categoryId) {
        Category category = categoryRepository.findOne(categoryId);

        if (category == null || category.getStatus() == Constants.StatusCodes.DELETED) {
            return null;
        } else {
            return category;
        }
    }

    @Override
    @RolesAllowed(Roles.ROLE_SHOP_ADMIN)
    public Category create(Category category) {
        userUtility.checkUserCanManageShop(category.getShopId());
        if (category.getStatus() == null) {
            category.setStatus(Constants.StatusCodes.ACTIVE);
        }
        if (category.getItemIds() == null) {
            category.setItemIds(new HashSet<>());
        }
        BeanValidator.validate(category);
        return categoryRepository.save(category);
    }

    @Override
    @RolesAllowed(Roles.ROLE_SHOP_ADMIN)
    public Category update(Category category) {
        userUtility.checkUserCanManageShop(category.getShopId());
        BeanValidator.validate(category);
        return  categoryRepository.save(category);
    }

    @Override
    @RolesAllowed({Roles.ROLE_SHOP_ADMIN})
    public Category delete(long categoryId) {

        Category category = categoryRepository.findOne(categoryId);
        userUtility.checkUserCanManageShop(category.getShopId());
        category.setStatus(Constants.StatusCodes.DELETED);
        category.setItemIds(new HashSet<>());
        return categoryRepository.save(category);
    }

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN, Roles.ROLE_X_ADMIN})
    public List<Category> list(Optional<Specification<Category>> specOptional, Optional<Pageable> pageableOptional) {
        List<Category> list = new ArrayList<>();

        Specification<Category> specification;

        if (specOptional.isPresent()) {
            specification = Specifications.where(CategorySpecifications.notDeleted()).and(specOptional.get());
        } else {
            specification = Specifications.where(CategorySpecifications.notDeleted());
        }

        if (pageableOptional.isPresent()) {
            categoryRepository.findAll(specification, pageableOptional.get()).forEach(list::add);
        } else {
            categoryRepository.findAll(specification).forEach(list::add);
        }

        return list;
    }

    private void checkItemOwnership(Set<Long> itemIds) {
        for (long itemId : itemIds) {
            Item item = itemService.get(itemId);
            if (item == null || item.getShopId() != userUtility.getUserShopId()) {
                throw new IllegalArgumentException(itemId + " is not a valid itemId");
            }
        }
    }
}
