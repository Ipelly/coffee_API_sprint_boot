package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.IngredientDao;
import com.xiaoslab.coffee.api.objects.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ipeli on 10/16/16.
 */
public class IngredientService implements IService<Ingredient> {

    @Autowired
    private IngredientDao ingredientDao;


    @Override
    public List<Ingredient> getAll() {
        return ingredientDao.Ingredients();
    }

    @Override
    public Ingredient get(String id) {
        return null;
    }

    @Override
    public Boolean Insert(Ingredient obj) {
        return null;
    }

    @Override
    public Boolean Update(Ingredient pbj) {
        return null;
    }

    @Override
    public Boolean Delete(Ingredient obj) {
        return null;
    }
}
