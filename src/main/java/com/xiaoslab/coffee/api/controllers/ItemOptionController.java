package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.ItemOption;
import com.xiaoslab.coffee.api.services.IService;
import com.xiaoslab.coffee.api.specifications.ItemOptionSpecifications;
import com.xiaoslab.coffee.api.utility.AppUtility;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

/**
 * Created by ipeli on 10/29/16.
 */


@RestController
@RequestMapping(path = Constants.V1 + Constants.SHOP_ENDPOINT + "/{shopId}" + Constants.ITEM_ENDPOINT +  "/{itemId}" + Constants.ITEMOPTION_ENDPOINT)
//@RequestMapping("/v1/itemoptions")
public class ItemOptionController {
    @Autowired
    private IService<ItemOption> itemOptionService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResponseEntity list(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) int size,
            @RequestParam(name = "search", defaultValue = "", required = false) String search,
            @RequestParam(name = "maxPrice", defaultValue = "500.00", required = false) BigDecimal maxPrice,
            @RequestParam(name = "minPrice",  defaultValue = "0.00",required = false) BigDecimal minPrice ) {

        Pageable pageable = new PageRequest(page, size);
        Specification<ItemOption> specification = Specifications.where(ItemOptionSpecifications.search(search))
                                                                .and(ItemOptionSpecifications.maxPrice(maxPrice))
                                                                .and(ItemOptionSpecifications.minPrice(minPrice));

        return ResponseEntity.ok(itemOptionService.list(Optional.of(specification), Optional.of(pageable)));
    }

    @RequestMapping(path = "/{itemOptionId}", method = RequestMethod.GET)
    public ResponseEntity get(@PathVariable long itemOptionId) {
        ItemOption itemOption = AppUtility.notFoundOnNull(itemOptionService.get(itemOptionId));
        return ResponseEntity.ok(itemOption);
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody ItemOption itemOption) {
        ItemOption createdItemOption = itemOptionService.create(itemOption);
        URI location = AppUtility.buildCreatedLocation(createdItemOption.getItemOptionId());
        return ResponseEntity.created(location).body(createdItemOption);
    }

    @RequestMapping(path = "/{itemoptionid}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable long itemoptionid, @RequestBody ItemOption itemOption) {
        AppUtility.notFoundOnNull(itemOptionService.get(itemoptionid));
        return ResponseEntity.ok(itemOptionService.update(itemOption));
    }

    @RequestMapping(path = "/{itemoptionid}", method=RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable long itemoptionid) {
        AppUtility.notFoundOnNull(itemOptionService.get(itemoptionid));
        itemOptionService.delete(itemoptionid);
        return ResponseEntity.noContent().build();
    }
}
