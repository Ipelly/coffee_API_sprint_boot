package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.Category;
import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.services.IService;
import com.xiaoslab.coffee.api.specifications.CategorySpecifications;
import com.xiaoslab.coffee.api.utility.AppUtility;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by ipeli on 10/15/17.
 */

@RestController
@RequestMapping(path = Constants.V1 + Constants.SHOP_ENDPOINT + "/{shopId}" + Constants.CATEGORY_ENDPOINT)
public class CategoryController {

    @Autowired
    IService<Category> categoryService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity list(@PathVariable long shopId,
           @RequestParam(name = "page", defaultValue = "0", required = false) int page,
           @RequestParam(name = "size", defaultValue = "10", required = false) int size,
           @RequestParam(name = "search", defaultValue = "", required = false) String search) {
        Pageable pageable = new PageRequest(page, size);
        Specification<Category> specification = Specifications
                .where(CategorySpecifications.categoryListForShop(shopId));
        return ResponseEntity.ok(categoryService.list(Optional.of(specification), Optional.of(pageable)));
    }

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.GET)
    public ResponseEntity get(@PathVariable long categoryId) {
        Category category = AppUtility.notFoundOnNull(categoryService.get(categoryId));
        return ResponseEntity.ok(category);
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Category category, @PathVariable long shopId) {
        category.setShopId(shopId);
        Category createdCategory = categoryService.create(category);
        URI location = AppUtility.buildCreatedLocation(createdCategory.getCategoryId());
        return ResponseEntity.created(location).body(createdCategory);
    }

    @RequestMapping(path = "/{categoryId}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable long categoryId, @PathVariable long shopId, @RequestBody Category category) {
        category.setShopId(shopId);
        AppUtility.notFoundOnNull(categoryService.get(categoryId));
        return ResponseEntity.ok(categoryService.update(category));
    }

    @RequestMapping(path = "/{categoryId}", method=RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable long categoryId) {
        AppUtility.notFoundOnNull(categoryService.get(categoryId));
        Category createdCategory = categoryService.delete(categoryId);
        if(Objects.isNull(createdCategory))
            return ResponseEntity.unprocessableEntity().build();
        return ResponseEntity.noContent().build();
    }
}
