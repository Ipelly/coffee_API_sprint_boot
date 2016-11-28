package com.xiaoslab.coffee.api.specifications;

import com.xiaoslab.coffee.api.objects.Addon;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.math.BigDecimal;

/**
 * Created by ipeli on 10/29/16.
 */
public class AddonSpecifications {
    public static Specification<Addon> isActive() {
        return (root, query, criteria) -> criteria.equal(root.get("status"), Constants.StatusCodes.ACTIVE);
    }

    public static Specification<Addon> notDeleted() {
        return (root, query, criteria) -> criteria.notEqual(root.get("status"), Constants.StatusCodes.DELETED);
    }

    public static Specification<Addon> search(String search) {
        return Specifications
                .where(searchName(search));
    }

    public static Specification<Addon> searchName(String name) {
        return (root, query, criteria) -> criteria.like(criteria.lower(root.get("name")), "%" + name + "%");
    }

    public static Specification<Addon> minPrice(BigDecimal min) {
        return (root, query, criteria) -> criteria.greaterThanOrEqualTo(root.get("price"), min);
    }

    public static Specification<Addon> maxPrice(BigDecimal max) {
        return (root, query, criteria) -> criteria.lessThanOrEqualTo (root.get("price"), max);
    }

    public static Specification<Addon> addonListForShop(long shopId) {
        return (root, query, criteria) -> criteria.equal(root.get("shopId"), shopId);
    }
}
