package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.ItemIngredientDao;
import com.xiaoslab.coffee.api.objects.ItemIngredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * Created by ipeli on 10/16/16.
 */
public class ItemIngredientService implements IService<ItemIngredient > {

    @Autowired
    private ItemIngredientDao itemIngredientDao;

    @Override
    public List<ItemIngredient> list() {
        return itemIngredientDao.ItemIngredients();
    }

    @Override
    public ItemIngredient get(long id) {
        return null;
    }

    @Override
    public ItemIngredient create(ItemIngredient obj) {
        return null;
    }

    @Override
    public ItemIngredient update(ItemIngredient pbj) {
        return null;
    }

    @Override
    public ItemIngredient delete(long obj) {
        return null;
    }

    @Override
    public List<ItemIngredient> list(Optional<Specification<ItemIngredient>> spec, Optional<Pageable> pageable) {
        return null;
    }
}
