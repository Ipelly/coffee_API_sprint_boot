package com.xiaoslab.coffee.api.specifications;

import com.xiaoslab.coffee.api.objects.ItemOption;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.math.BigDecimal;

public class ItemOptionSpecifications {
    public static Specification<ItemOption> isActive() {
        return (root, query, criteria) -> criteria.equal(root.get("status"), Constants.StatusCodes.ACTIVE);
    }

    public static Specification<ItemOption> notDeleted() {
        return (root, query, criteria) -> criteria.notEqual(root.get("status"), Constants.StatusCodes.DELETED);
    }

    public static Specification<ItemOption> search(String search) {
        return Specifications.where(searchName(search));
    }

    public static Specification<ItemOption> searchName(String name) {
        return (root, query, criteria) -> criteria.like(criteria.lower(root.get("name")), "%" + name + "%");
    }

    public static Specification<ItemOption> minPrice(BigDecimal min) {
        return (root, query, criteria) -> criteria.greaterThanOrEqualTo(root.get("price"), min);
    }

    public static Specification<ItemOption> maxPrice(BigDecimal max) {
        return (root, query, criteria) -> criteria.lessThanOrEqualTo (root.get("price"), max);
    }

    public static Specification<ItemOption> filterByItemId(long itemId) {
        return (root, query, criteria) -> criteria.equal(root.get("itemId"), itemId);
    }
}
