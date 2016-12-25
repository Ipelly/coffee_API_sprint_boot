package com.xiaoslab.coffee.api.specifications;

import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> hasEmailAddress(String emailAddress) {
        return (root, query, criteria) -> criteria.equal(root.get("emailAddress"), emailAddress);
    }

    public static Specification<User> isActive() {
        return (root, query, criteria) -> criteria.equal(root.get("status"), Constants.StatusCodes.ACTIVE);
    }

    public static Specification<User> isStatus(Constants.StatusCodes statusFilter) {
        return (root, query, criteria) -> criteria.equal(root.get("status"), statusFilter);
    }

    public static Specification<User> isNotDeleted() {
        return (root, query, criteria) -> criteria.notEqual(root.get("status"), Constants.StatusCodes.DELETED);
    }

    public static Specification<User> isNotStatus(Constants.StatusCodes notStatusFilter) {
        return (root, query, criteria) -> criteria.notEqual(root.get("status"), notStatusFilter);
    }

    public static Specification<User> hasProviderUserId(String providerUserId, Constants.LoginProviderType providerType) {
        return (root, query, criteria) -> criteria.and(
                criteria.equal(root.get("providerUserId"), providerUserId),
                criteria.equal(root.get("providerType"), providerType)
        );
    }

    public static Specification<User> notEmptyPassword() {
        return (root, query, criteria) -> criteria.isNotNull(root.get("password"));
    }

    public static Specification<User> isShopUser() {
        return (root, query, criteria) -> criteria.greaterThan(root.get("shopId"), 0);
    }

    public static Specification<User> isNotShopUser() {
        return (root, query, criteria) -> criteria.or(
                criteria.equal(root.get("shopId"), 0),
                criteria.isNull(root.get("shopId"))
        );
    }

    public static Specification<User> belongsToShopId(long shopId) {
        return (root, query, criteria) -> criteria.equal(root.get("shopId"), shopId);
    }

}
