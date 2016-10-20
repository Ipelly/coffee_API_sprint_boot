package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.Addon;
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
@RequestMapping("/v1/addons")
public class AddonController {

    @Autowired
    IService<Addon, String> addonService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object getAddon() {
        return addonService.getAll();
    }

    @RequestMapping(value = "/{addonsId}", method = RequestMethod.GET)
    //@PathParam
    public Object getShop(@PathVariable int shopId) {
        return null;
    }
}
