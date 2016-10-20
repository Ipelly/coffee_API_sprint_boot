package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.IngredientDao;
import com.xiaoslab.coffee.api.objects.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ipeli on 10/16/16.
 */
public class IngredientService implements IService<Ingredient,String> {

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
    public Ingredient Create(Ingredient obj) {
        return null;
    }

    @Override
    public Ingredient Update(Ingredient pbj) {
        return null;
    }

    @Override
    public Ingredient Delete(String obj) {
        return null;
    }
}
