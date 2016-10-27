package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.services.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ipeli on 10/14/16.
 */


@RestController
@RequestMapping("/v1/shops")
public class ShopController {

    @Autowired
    IService<Shop> shopService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object getShops() {
        return shopService.list();
    }

    @RequestMapping(value = "/{shopId}", method = RequestMethod.GET)
    public Object getShop(@PathVariable long shopId) {
        return shopService.get(shopId);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Object getShops(@RequestBody Shop shop) {
        return shopService.create(shop);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public Object update(@RequestBody Shop shop) {
        return shopService.update(shop);
    }

    @RequestMapping(method=RequestMethod.DELETE, value="{id}")
    public void delete(@PathVariable long id) {

    }
}

