package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.services.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ipeli on 10/14/16.
 */


@RestController
@RequestMapping("/v1/shops")
public class ShopController {

    @Autowired
    IService shopService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object getShops() {
        return shopService.getAll();
    }

    //--- Need to talk with rafaat hossain
    @RequestMapping(value = "/{shopId}", method = RequestMethod.GET)
    //@PathParam
    public Object getShop(@PathVariable int shopId) {
        return shopService.get(Integer.toString(shopId));
        //return null;
    }

/*
    //--- Need to talk with rafaat hossain
    @RequestMapping(value = "/shops/?lat=60&long=56&radius=5/", method = RequestMethod.GET)
    public Object getShops(int shopId, BigDecimal lat, BigDecimal lng, double radius) {
        return shopService.getAll().stream()
                .filter(p -> p.getLatitude().equals(lat) & p.getLongitute().equals(lng))
                .collect(Collectors.toList());
    }

    //--- Need to talk with rafaat hossain
    @RequestMapping(value = "shops/?search=deli&page=1&pageSize=20", method = RequestMethod.GET)
    public Object getShops(int shopId, String shopName, int sPage, int ePage) {
        return shopService.getAll().stream()
                .filter(p -> p.getName().equals(shopName))
                .collect(Collectors.toList());
    }
    */

}

