package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.services.IService;
import com.xiaoslab.coffee.api.specifications.ShopSpecifications;
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

import java.math.BigDecimal;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by ipeli on 10/14/16.
 */


@RestController
@RequestMapping(path = Constants.V1 + Constants.SHOP_ENDPOINT)
public class ShopController {

    @Autowired
    private IService<Shop> shopService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResponseEntity list(
            @RequestParam(name = "size", required = true) int size,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "rating", required = false) Integer rating,
            @RequestParam(name = "zip", required = false) String zip,
            @RequestParam(name = "lat", required = false) BigDecimal lat,
            @RequestParam(name = "lon", required = false) BigDecimal lon,
            @RequestParam(name = "radius", required = false) BigDecimal radius) {

        Pageable pageable = AppUtility.createPageRequest(page, size);

        Specification<Shop> specification = ShopSpecifications.notDeleted();
        if (StringUtils.isNotBlank(search)) {
            specification = Specifications.where(specification).and(ShopSpecifications.search(search));
        }
        if (!Objects.isNull(rating)) {
            specification = Specifications.where(specification).and(ShopSpecifications.minimumRating(rating));
        }
        if (!Objects.isNull(lat) && !Objects.isNull(lon) && !Objects.isNull(radius)) {
            specification = Specifications.where(specification).and(ShopSpecifications.withinRadius(lat, lon, radius));
        }
        return ResponseEntity.ok(shopService.list(Optional.of(specification), Optional.of(pageable)));
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

