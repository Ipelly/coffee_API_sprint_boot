package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.ItemIngredient;
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
@RequestMapping("/v1/itemingredients/")
public class ItemIngredientController {

    @Autowired
    IService<ItemIngredient> itemIngredientService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object getItemIngredients() {
        return itemIngredientService.list();
    }

    @RequestMapping(value = "/{itemingredientId}", method = RequestMethod.GET)
    //@PathParam
    public Object getItemIngredient(@PathVariable int itemingredientId) {
        return null;
    }
}
