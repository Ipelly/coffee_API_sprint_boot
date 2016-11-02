package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.services.IService;
import com.xiaoslab.coffee.api.specifications.ItemSpecifications;
import com.xiaoslab.coffee.api.utility.AppUtility;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

/**
 * Created by ipeli on 10/15/16.
 */

@RestController
@RequestMapping(path = Constants.V1 + Constants.ITEM_ENDPOINT)
public class ItemController {

    @Autowired
    IService<Item> itemService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity list(
           @RequestParam(name = "page", defaultValue = "0", required = false) int page,
           @RequestParam(name = "size", defaultValue = "10", required = false) int size,
           @RequestParam(name = "search", defaultValue = "", required = false) String search) {

        Pageable pageable = new PageRequest(page, size);
        Specification<Item> specification = Specifications
                .where(ItemSpecifications.search(search));
        return ResponseEntity.ok(itemService.list(Optional.of(specification), Optional.of(pageable)));
    }

    @RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
    public ResponseEntity get(@PathVariable long itemId) {
        Item item = AppUtility.notFoundOnNull(itemService.get(itemId));
        return ResponseEntity.ok(item);
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Item item) {
        Item createdItem = itemService.create(item);
        URI location = AppUtility.buildCreatedLocation(createdItem.getItem_id());
        return ResponseEntity.created(location).body(createdItem);
    }

    @RequestMapping(path = "/{itemId}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable long itemId, @RequestBody Item item) {
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
