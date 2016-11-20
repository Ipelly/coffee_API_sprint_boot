package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.Addon;
import com.xiaoslab.coffee.api.services.IService;
import com.xiaoslab.coffee.api.specifications.AddonSpecifications;
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
@RequestMapping(path = Constants.V1 + Constants.SHOP_ENDPOINT + "/{shopId}" + Constants.ADDON_ENDPOINT)
public class AddonController {

    @Autowired
    IService<Addon> addonService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity list(@PathVariable long shopId,
           @RequestParam(name = "page", defaultValue = "0", required = false) int page,
           @RequestParam(name = "size", defaultValue = "10", required = false) int size,
           @RequestParam(name = "search", required = false) String search) {

        Pageable pageable = new PageRequest(page, size);
        Specification<Addon> specification = Specifications.where(AddonSpecifications.addonListForShop(shopId))
                                                            .and(AddonSpecifications.notDeleted () );

        if (StringUtils.isNotBlank(search)) {
            specification = Specifications.where(specification).and(AddonSpecifications.search(search));
        }
        return ResponseEntity.ok(addonService.list(Optional.of(specification), Optional.of(pageable)));
    }

    @RequestMapping(value = "/{addonId}", method = RequestMethod.GET)
    public ResponseEntity get(@PathVariable long addonId) {
        Addon addon = AppUtility.notFoundOnNull(addonService.get(addonId));
        return ResponseEntity.ok(addon);
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Addon addon) {
        Addon createdAddon = addonService.create(addon);
        URI location = AppUtility.buildCreatedLocation(createdAddon.getAddon_id());
        return ResponseEntity.created(location).body(createdAddon);
    }

    @RequestMapping(path = "/{addonId}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable long addonId, @RequestBody Addon addon) {
        AppUtility.notFoundOnNull(addonService.get(addonId));
        return ResponseEntity.ok(addonService.update(addon));
    }

    @RequestMapping(path = "/{addonId}", method=RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable long addonId) {
        AppUtility.notFoundOnNull(addonService.get(addonId));
        addonService.delete(addonId);
        return ResponseEntity.noContent().build();
    }
}
