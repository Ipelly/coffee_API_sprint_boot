package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.ItemOption;
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
@RequestMapping(path = Constants.V1 + Constants.SHOP_ENDPOINT + "/{shopId}" + Constants.ITEM_ENDPOINT +  "/{itemId}" + Constants.OPTION_ENDPOINT)
public class ItemOptionController {

    @Autowired
    private IService<ItemOption> itemOptionService;

    @Autowired
    private IService<Item> itemService;

    @Autowired
    private IService<Shop> shopService;

    // retrieve all options in one call
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResponseEntity list(@PathVariable long shopId, @PathVariable long itemId) {
        checkShopAndItem(shopId, itemId);
        return ResponseEntity.ok(itemOptionService.list(itemId));
    }

    // update all options in one call
    @RequestMapping(path = "/", method = RequestMethod.PUT)
    public ResponseEntity updateAll(@PathVariable long shopId, @PathVariable long itemId, @RequestBody ItemOption[] itemOptions) {
        checkShopAndItem(shopId, itemId);
        List<ItemOption> listOfOptions = Arrays.asList(itemOptions);
        return ResponseEntity.ok(itemOptionService.updateAll(itemId, listOfOptions));
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
