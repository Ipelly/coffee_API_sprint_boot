package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.Ingredient;
import com.xiaoslab.coffee.api.services.IService;
import com.xiaoslab.coffee.api.specifications.IngredientSpecifications;
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
 * Created by ipeli on 10/29/16.
 */


@RestController
@RequestMapping(path = Constants.V1 + Constants.INGREDIENT_ENDPOINT)
//@RequestMapping("/v1/ingredients")
public class IngredientController {
    @Autowired
    private IService<Ingredient> ingredientService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResponseEntity list(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) int size,
            @RequestParam(name = "search", required = false) String search) {

        Pageable pageable = new PageRequest(page, size);


        Specification<Ingredient> specification = IngredientSpecifications.notDeleted();
        if (StringUtils.isNotBlank(search)) {
            specification = Specifications.where(specification).and(IngredientSpecifications.search(search));
        }
       
        return ResponseEntity.ok(ingredientService.list(Optional.of(specification), Optional.of(pageable)));
    }

    @RequestMapping(path = "/{ingredientId}", method = RequestMethod.GET)
    public ResponseEntity get(@PathVariable long ingredientId) {
        Ingredient ingredient = AppUtility.notFoundOnNull(ingredientService.get(ingredientId));
        return ResponseEntity.ok(ingredient);
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Ingredient ingredient) {
        Ingredient createdIngredient = ingredientService.create(ingredient);
        URI location = AppUtility.buildCreatedLocation(createdIngredient.getIngredientId());
        return ResponseEntity.created(location).body(createdIngredient);
    }

    @RequestMapping(path = "/{ingredientid}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable long ingredientid, @RequestBody Ingredient ingredient) {
        AppUtility.notFoundOnNull(ingredientService.get(ingredientid));
        return ResponseEntity.ok(ingredientService.update(ingredient));
    }

    @RequestMapping(path = "/{ingredientid}", method=RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable long ingredientid) {
        AppUtility.notFoundOnNull(ingredientService.get(ingredientid));
        ingredientService.delete(ingredientid);
        return ResponseEntity.noContent().build();
    }
}
