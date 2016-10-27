package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.IngredientDao;
import com.xiaoslab.coffee.api.objects.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * Created by ipeli on 10/16/16.
 */
public class IngredientService implements IService<Ingredient> {

    @Autowired
    private IngredientDao ingredientDao;


    @Override
    public List<Ingredient> list() {
        return ingredientDao.Ingredients();
    }

    @Override
    public Ingredient get(long id) {
        return null;
    }

    @Override
    public Ingredient create(Ingredient obj) {
        return null;
    }

    @Override
    public Ingredient update(Ingredient pbj) {
        return null;
    }

    @Override
    public Ingredient delete(long id) {
        return null;
    }

    @Override
    public List<Ingredient> list(Optional<Specification<Ingredient>> spec, Optional<Pageable> pageable) {
        return null;
    }
}
