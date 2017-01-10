package com.xiaoslab.coffee.api.specifications;

import com.xiaoslab.coffee.api.objects.Ingredient;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

/**
 * Created by ipeli on 10/29/16.
 */
public class IngredientSpecifications {
    public static Specification<Ingredient> isActive() {
        return (root, query, criteria) -> criteria.equal(root.get("status"), Constants.StatusCodes.ACTIVE);
    }

    public static Specification<Ingredient> notDeleted() {
        return (root, query, criteria) -> criteria.notEqual(root.get("status"), Constants.StatusCodes.DELETED);
    }

    public static Specification<Ingredient> search(String search) {
        return Specifications
                .where(searchName(search));
    }

    public static Specification<Ingredient> searchName(String name) {
        return (root, query, criteria) -> criteria.like(criteria.lower(root.get("name")), "%" + name + "%");
    }
}
