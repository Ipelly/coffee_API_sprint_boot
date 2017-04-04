package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Ingredient;
import com.xiaoslab.coffee.api.repository.IngredientRepository;
import com.xiaoslab.coffee.api.security.Roles;
import com.xiaoslab.coffee.api.specifications.IngredientSpecifications;
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
public class IngredientService implements IService<Ingredient> {

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    UserUtility userUtility;

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN})
    public List<Ingredient> list() {
        return list(Optional.empty(), Optional.empty());
    }

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN,Roles.ROLE_X_ADMIN})
    public Ingredient get(long ingredientId) {
        Ingredient ingredient = ingredientRepository.findOne(ingredientId);
        if (ingredient == null || ingredient.getStatus() == Constants.StatusCodes.DELETED) {
            return null;
        } else {
            return ingredient;
        }
    }

    @Override
    @RolesAllowed({Roles.ROLE_SHOP_ADMIN, Roles.ROLE_X_ADMIN})
    public Ingredient create(Ingredient ingredient) {
        if (ingredient.getStatus() == null) {
            ingredient.setStatus(Constants.StatusCodes.INACTIVE);
        }
        return ingredientRepository.save(ingredient);
    }

    @Override
    @RolesAllowed({ Roles.ROLE_X_ADMIN})
    public Ingredient update(Ingredient ingredient) {
        return  ingredientRepository.save(ingredient);
    }

    @Override
    @RolesAllowed({Roles.ROLE_X_ADMIN})
    public Ingredient delete(long ingredientId) {
        Ingredient ingredient = ingredientRepository.findOne(ingredientId);
        ingredient.setStatus(Constants.StatusCodes.DELETED);
        return ingredientRepository.save(ingredient);
    }

    @Override
    @RolesAllowed({Roles.ROLE_USER, Roles.ROLE_SHOP_USER, Roles.ROLE_SHOP_ADMIN, Roles.ROLE_X_ADMIN})
    public List<Ingredient> list(Optional<Specification<Ingredient>> specOptional, Optional<Pageable> pageableOptional) {
        List<Ingredient> list = new ArrayList<>();

        Specification<Ingredient> specification;

        if (specOptional.isPresent()) {
            specification = Specifications.where(IngredientSpecifications.notDeleted()).and(specOptional.get());
        } else {
            specification = Specifications.where(IngredientSpecifications.notDeleted());
        }
        if (pageableOptional.isPresent()) {
            ingredientRepository.findAll(specification, pageableOptional.get()).forEach(list::add);
        } else {
            ingredientRepository.findAll(specification).forEach(list::add);
        }
        return list;
    }
}
