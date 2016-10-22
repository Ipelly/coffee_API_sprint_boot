package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.Item;
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
@RequestMapping("/v1/items/")
public class ItemController {

    @Autowired
    IService<Item> itemService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object getItem() {
        return itemService.getAll();
    }

    @RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
    //@PathParam
    public Object getItems(@PathVariable int itemId) {
        return null;
    }
}
