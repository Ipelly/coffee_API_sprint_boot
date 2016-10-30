package com.xiaoslab.coffee.api.specifications;

import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.math.BigDecimal;

public class ShopSpecifications {

    public static Specification<Shop> isActive() {
        return (root, query, criteria) -> criteria.equal(root.get("status"), Constants.StatusCodes.ACTIVE);
    }

    public static Specification<Shop> notDeleted() {
        return (root, query, criteria) -> criteria.notEqual(root.get("status"), Constants.StatusCodes.DELETED);
    }

    public static Specification<Shop> search(String search) {
        return Specifications
                .where(searchName(search))
                .or(searchAddress1(search))
                .or(searchAddress2(search))
                .or(searchCity(search));
    }

    public static Specification<Shop> searchName(String name) {
        return (root, query, criteria) -> criteria.like(criteria.lower(root.get("name")), "%" + name + "%");
    }

    public static Specification<Shop> searchAddress1(String address1) {
        return (root, query, criteria) -> criteria.like(criteria.lower(root.get("address1")), "%" + address1 + "%");
    }

    public static Specification<Shop> searchAddress2(String address2) {
        return (root, query, criteria) -> criteria.like(criteria.lower(root.get("address2")), "%" + address2 + "%");
    }

    public static Specification<Shop> searchCity(String city) {
        return (root, query, criteria) -> criteria.like(criteria.lower(root.get("city")), "%" + city + "%");
    }

    public static Specification<Shop> minimumRating(int rating) {
        return (root, query, criteria) -> criteria.greaterThan(root.get("rating"), rating);
    }

    public static Specification<Shop> withinRadius(BigDecimal latitude, BigDecimal longitude, BigDecimal miles) {
        //FIXME: implement the logic of finding shop within the radius
        return (root, query, criteria) -> criteria.greaterThan(root.get("rating"), 0);
    }
}