package com.xiaoslab.coffee.api.specifications;

import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ShopSpecifications {

    public static Specification<Shop> isActive() {
        return (root, query, criteria) -> criteria.equal(root.get("status"), Constants.StatusCodes.ACTIVE);
    }

    public static Specification<Shop> notDeleted() {
        return (root, query, criteria) -> criteria.notEqual(root.get("status"), Constants.StatusCodes.DELETED);
    }

    public static Specification<Shop> searchName(String name) {
        return (root, query, criteria) -> criteria.like(criteria.lower(root.get("name")), "%" + name + "%");
    }

    public static Specification<Shop> minimumRating(int rating) {
        return (root, query, criteria) -> criteria.greaterThan(root.get("rating"), rating);
    }

    public static Specification<Shop> withinRadius(BigDecimal latitude, BigDecimal longitude, BigDecimal miles) {
        throw new NotImplementedException("not implemented"); //TODO
    }
}