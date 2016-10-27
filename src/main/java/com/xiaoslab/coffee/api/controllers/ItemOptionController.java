package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.ItemOption;
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
@RequestMapping("/v1/itemoptions/")
public class ItemOptionController {

    @Autowired
    IService<ItemOption> itemOptionService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object getItemoptions() {
        return itemOptionService.list();
    }

    @RequestMapping(value = "/{itemoptionsId}", method = RequestMethod.GET)
    //@PathParam
    public Object getItemoption(@PathVariable int itemoptionId) {
        return null;
    }
}
