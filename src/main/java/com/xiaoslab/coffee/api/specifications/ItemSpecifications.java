package com.xiaoslab.coffee.api.specifications;

import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.math.BigDecimal;

/**
 * Created by islamma on 11/02/17.
 */
public class ItemSpecifications {

    public static Specification<Item> isActive() {
        return (root, query, criteria) -> criteria.equal(root.get("status"), Constants.StatusCodes.ACTIVE);
    }

    public static Specification<Item> notDeleted() {
        return (root, query, criteria) -> criteria.notEqual(root.get("status"), Constants.StatusCodes.DELETED);
    }

    public static Specification<Item> searchName(String name) {
        return (root, query, criteria) -> criteria.like(criteria.lower(root.get("name")), "%" + name + "%");
    }

    public static Specification<Item> minimumRating(int rating) {
        return (root, query, criteria) -> criteria.greaterThan(root.get("rating"), rating);
    }

    public static Specification<Item> search(String search) {
        return Specifications
                .where(searchName(search));
    }

    public static Specification<Item> itemListForCategory(long categoryId) {
        return (root, query, criteria) -> criteria.equal(criteria.lower(root.get("category_id")),categoryId);
    }

    public static Specification<Item> itemListForShop(long shopId) {
        return (root, query, criteria) -> criteria.equal(criteria.lower(root.get("shop_id")),shopId);
    }

    public static Specification<Item> itemListForCategoryUnderAShop(long shopId, long categoryId) {
        return (root, query, criteria) -> criteria.and(
                criteria.equal(root.get("shop_id"), shopId),
                criteria.equal(root.get("category_id"), categoryId)
        );
    }

}