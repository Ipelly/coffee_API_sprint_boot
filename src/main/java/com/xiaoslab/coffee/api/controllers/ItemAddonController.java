package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.ItemAddon;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.services.IService;
import com.xiaoslab.coffee.api.utility.AppUtility;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping(path = Constants.V1 + Constants.SHOP_ENDPOINT + "/{shopId}" + Constants.ITEM_ENDPOINT +  "/{itemId}" + Constants.ADDON_ENDPOINT)
public class ItemAddonController {

    @Autowired
    private IService<ItemAddon> itemAddonService;

    @Autowired
    private IService<Item> itemService;

    @Autowired
    private IService<Shop> shopService;

    // retrieve all addons in one call
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResponseEntity list(@PathVariable long shopId, @PathVariable long itemId) {
        checkShopAndItem(shopId, itemId);
        return ResponseEntity.ok(itemAddonService.list(itemId));
    }

    // update all addons in one call
    @RequestMapping(path = "/", method = RequestMethod.PUT)
    public ResponseEntity updateAll(@PathVariable long shopId, @PathVariable long itemId, @RequestBody ItemAddon[] itemAddons) {
        checkShopAndItem(shopId, itemId);
        List<ItemAddon> listOfAddons = Arrays.asList(itemAddons);
        return ResponseEntity.ok(itemAddonService.updateAll(itemId, listOfAddons));
    }

    private void checkShopAndItem(long shopId, long itemId) {
        Shop shop = shopService.get(shopId);
        AppUtility.notFoundOnNull(shop);
        Item item = itemService.get(itemId);
        AppUtility.notFoundOnNull(item);
        if (!Objects.equals(item.getShopId(), shopId)) {
            throw new EntityNotFoundException();
        }
    }

}
