package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.services.IService;
import com.xiaoslab.coffee.api.specifications.ItemSpecifications;
import com.xiaoslab.coffee.api.utility.AppUtility;
import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

/**
 * Created by ipeli on 10/15/16.
 */

@RestController
@RequestMapping(path = Constants.V1 + Constants.SHOP_ENDPOINT + "/{shopId}" + Constants.ITEM_ENDPOINT )
public class ItemController {

    @Autowired
    IService<Item> itemService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity list(@PathVariable long shopId,
                               @RequestParam(name = "size", required = true) int size,
                               @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                               @RequestParam(name = "categoryId", defaultValue = "0", required = false) long categoryId,
                               @RequestParam(name = "search", required = false) String search) {

        Pageable pageable = AppUtility.createPageRequest(page, size);
        Specification<Item> specification;

        if(shopId > 0 && categoryId > 0){
//            specification = Specifications.where(ItemSpecifications.itemListForCategoryUnderAShop(shopId,categoryId))
//                    .and(ItemSpecifications.notDeleted ());
            specification = Specifications.where(ItemSpecifications.itemListForShop(shopId))
                    .and(ItemSpecifications.itemListForCategory (categoryId))
                    .and(ItemSpecifications.notDeleted ());


        }else {
            specification = Specifications.where(ItemSpecifications.itemListForShop(shopId))
                    .and(ItemSpecifications.notDeleted());
        }

        if (StringUtils.isNotBlank(search)) {
            specification = Specifications.where(specification).and(ItemSpecifications.search(search));
        }
        return ResponseEntity.ok(itemService.list(Optional.of(specification), Optional.of(pageable)));
    }

    @RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
    public ResponseEntity get(@PathVariable long itemId) {
        Item Item = AppUtility.notFoundOnNull(itemService.get(itemId));
        return ResponseEntity.ok(Item);
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Item item, @PathVariable long shopId) {
        item.setShopId(shopId);
        Item createdItem = itemService.create(item);
        URI location = AppUtility.buildCreatedLocation(createdItem.getItemId ());
        return ResponseEntity.created(location).body(createdItem);
    }

    @RequestMapping(path = "/{itemId}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable long itemId, @PathVariable long shopId, @RequestBody Item item) {
        item.setShopId(shopId);
        AppUtility.notFoundOnNull(itemService.get(itemId));
        return ResponseEntity.ok(itemService.update(item));
    }

    @RequestMapping(path = "/{itemId}", method=RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable long itemId) {
        AppUtility.notFoundOnNull(itemService.get(itemId));
        itemService.delete(itemId);
        return ResponseEntity.noContent().build();
    }
}
