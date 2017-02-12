package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Category;
import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.repository.CategoryRepository;
import com.xiaoslab.coffee.api.repository.ItemRepository;
import com.xiaoslab.coffee.api.security.Roles;
import com.xiaoslab.coffee.api.specifications.CategorySpecifications;
import com.xiaoslab.coffee.api.specifications.ItemSpecifications;
import com.xiaoslab.coffee.api.utility.CategoryUtility;
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
public class CategoryService implements IService<Category> {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ItemRepository itemRepository;

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
        Category category = categoryRepository.getOne(categoryId);

        if (category == null || category.getStatus() == Constants.StatusCodes.DELETED) {
            return null;
        } else {
            return category;
        }
    }

    @Override
    @RolesAllowed(Roles.ROLE_SHOP_ADMIN)
    public Category create(Category category) {
        userUtility.checkUserCanManageShop(category.getShop_id());
        if (category.getStatus() == null) {
            category.setStatus(Constants.StatusCodes.ACTIVE);
        }
        return categoryRepository.save(category);
    }

    @Override
    @RolesAllowed(Roles.ROLE_SHOP_ADMIN)
    public Category update(Category category) {
        return  categoryRepository.save(category);
    }

    @Override
    @RolesAllowed({Roles.ROLE_SHOP_ADMIN})
    public Category delete(long categoryId) {

        Category category = categoryRepository.findOne(categoryId);
        userUtility.checkUserCanManageShop(category.getShop_id());
        if(categoryUtility.checkUserDeleteCategory(items(categoryId))) {
            category.setStatus(Constants.StatusCodes.DELETED);
            return categoryRepository.save(category);
        }else{
            return null;
        }
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

    @RolesAllowed({ Roles.ROLE_SHOP_ADMIN})
    private List<Item> items(long categoryId) {
        List<Item> list = new ArrayList<>();

        Specification<Item> itemSpecification = Specifications.where(ItemSpecifications.itemListForCategory(categoryId));
        itemRepository.findAll(itemSpecification).forEach(list::add);
        return list;
    }

}
