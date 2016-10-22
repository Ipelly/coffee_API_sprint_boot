package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.ItemIngredientDao;
import com.xiaoslab.coffee.api.objects.ItemIngredient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ipeli on 10/16/16.
 */
public class ItemIngredientService implements IService<ItemIngredient > {

    @Autowired
    private ItemIngredientDao itemIngredientDao;

    @Override
    public List<ItemIngredient> getAll() {
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
}
