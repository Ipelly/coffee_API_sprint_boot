package com.xiaoslab.coffee.api;

import com.xiaoslab.coffee.api.objects.MenuItem;
import com.xiaoslab.coffee.api.services.FacebookService;
import com.xiaoslab.coffee.api.services.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

// git practice/


@RestController
@RequestMapping("/v1/")
public class RequestController {

    @Autowired
    MenuItemService menuItemService;

//    @Autowired
//    ShopService shopService;

    @Autowired
    private FacebookService facebookService;
/*
    @RequestMapping(value = "/shops", method = RequestMethod.GET)
    public Object getShops() {
        return shopService.listshops();
    }

    //--- Need to talk with rafaat hossain
    @RequestMapping(value = "/shops/{shopId}", method = RequestMethod.GET)
    // @PathParam
    public Object getShop(@PathVariable int shopId) {
        return shopService.listshops().stream().filter(p -> p.getShopID() == shopId).collect(Collectors.toList());
        //return shopService.listshops();
    }

    //--- Need to talk with rafaat hossain
    @RequestMapping(value = "/shops/?lat=60&long=56&radius=5/", method = RequestMethod.GET)
    public Object getShops(int shopId, BigDecimal lat, BigDecimal lng, double radius) {
        return shopService.listshops().stream()
                .filter(p -> p.getLatitude().equals(lat) & p.getLongitute().equals(lng))
                .collect(Collectors.toList());
    }

    //--- Need to talk with rafaat hossain
    @RequestMapping(value = "shops/?search=deli&page=1&pageSize=20", method = RequestMethod.GET)
    public Object getShops(int shopId, String shopName, int sPage, int ePage) {
        return shopService.listshops().stream()
                .filter(p -> p.getName().equals(shopName))
                .collect(Collectors.toList());
    }
*/

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public Object getStatus() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "running");
        return response;
    }

    @RequestMapping(value = "/test-menu", method = RequestMethod.GET)
    public Object getTestMenu() {
        return menuItemService.listMenuItems();
    }

    @RequestMapping(value = "/test-menu", method = RequestMethod.POST)
    public Object createTestMenu(MenuItem menuItem) {
        menuItemService.create(menuItem);
        return menuItemService.listMenuItems();
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public Object getProfile() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


}
