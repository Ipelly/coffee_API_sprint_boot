package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.Ingredient;
import com.xiaoslab.coffee.api.services.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ipeli on 10/15/16.
 */

@RestController
@RequestMapping("/v1/ingredients/")
public class IngredientController {

    @Autowired
    IService<Ingredient, String> ingredientService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object getIngredients() {
        return ingredientService.getAll();
    }

    @RequestMapping(value = "/{ingredientsId}", method = RequestMethod.GET)
    //@PathParam
    public Object getIngredient(@PathVariable int IngredientId) {
        return null;
    }
}
