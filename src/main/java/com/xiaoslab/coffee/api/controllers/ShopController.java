package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.services.IService;
import com.xiaoslab.coffee.api.utility.AppUtility;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * Created by ipeli on 10/14/16.
 */


@RestController
@RequestMapping(path = Constants.V1 + Constants.SHOP_ENDPOINT)
public class ShopController {

    @Autowired
    private IService<Shop> shopService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResponseEntity list() {
        return ResponseEntity.ok(shopService.list());
    }

    @RequestMapping(path = "/{shopId}", method = RequestMethod.GET)
    public ResponseEntity get(@PathVariable long shopId) {
        Shop shop = AppUtility.notFoundOnNull(shopService.get(shopId));
        return ResponseEntity.ok(shop);
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Shop shop) {
        Shop createdShop = shopService.create(shop);
        URI location = AppUtility.buildCreatedLocation(createdShop.getShopId());
        return ResponseEntity.created(location).body(createdShop);
    }

    @RequestMapping(path = "/{shopId}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable long shopId, @RequestBody Shop shop) {
        AppUtility.notFoundOnNull(shopService.get(shopId));
        return ResponseEntity.ok(shopService.update(shop));
    }

    @RequestMapping(path = "/{shopId}", method=RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable long shopId) {
        AppUtility.notFoundOnNull(shopService.get(shopId));
        shopService.delete(shopId);
        return ResponseEntity.noContent().build();
    }
}

