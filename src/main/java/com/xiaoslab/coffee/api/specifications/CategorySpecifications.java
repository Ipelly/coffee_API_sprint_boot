package com.xiaoslab.coffee.api.specifications;

import com.xiaoslab.coffee.api.objects.Category;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

/**
 * Created by Ipeli on 11/1/16.
 */
public class CategorySpecifications {

    public static Specification<Category> isActive() {
        return (root, query, criteria) -> criteria.equal(root.get("status"), Constants.StatusCodes.ACTIVE);
    }

    public static Specification<Category> notDeleted() {
        return (root, query, criteria) -> criteria.notEqual(root.get("status"), Constants.StatusCodes.DELETED);
    }

    public static Specification<Category> searchName(String name) {
        return (root, query, criteria) -> criteria.like(criteria.lower(root.get("name")), "%" + name + "%");
    }

    public static Specification<Category> search(String search) {
        return Specifications
                .where(searchName(search));
    }

    public static Specification<Category> itemListForCategory(long categoryID) {
        return (root, query, criteria) -> criteria.equal(criteria.lower(root.get("shop_id")),categoryID);
        //return Specifications.where(searchName(search));
    }
    public static Specification<Category> categoryListForShop(long shopID) {
        return (root, query, criteria) -> criteria.equal(criteria.lower(root.get("shop_id")),shopID);
        //return Specifications.where(searchName(search));
    }

}