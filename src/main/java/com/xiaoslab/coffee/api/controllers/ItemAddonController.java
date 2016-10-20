package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.ItemAddon;
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
@RequestMapping("/v1/itemaddons/")
public class ItemAddonController {

    @Autowired
    IService<ItemAddon, String> itemAddonService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object getIitemAddons() {
        return itemAddonService.getAll();
    }

    @RequestMapping(value = "/{itemaddonId}", method = RequestMethod.GET)
    //@PathParam
    public Object getIitemAddon(@PathVariable int itemaddonId) {
        return null;
    }
}
