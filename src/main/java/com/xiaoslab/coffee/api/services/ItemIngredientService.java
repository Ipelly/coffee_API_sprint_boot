package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.ItemIngredientDao;
import com.xiaoslab.coffee.api.objects.ItemIngredient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ipeli on 10/16/16.
 */
public class ItemIngredientService implements IService<ItemIngredient, String > {

    @Autowired
    private ItemIngredientDao itemIngredientDao;

    @Override
    public List<ItemIngredient> getAll() {
        return itemIngredientDao.ItemIngredients();
    }

    @Override
    public ItemIngredient get(String id) {
        return null;
    }

    @Override
    public ItemIngredient Create(ItemIngredient obj) {
        return null;
    }

    @Override
    public ItemIngredient Update(ItemIngredient pbj) {
        return null;
    }

    @Override
    public ItemIngredient Delete(String obj) {
        return null;
    }
}
