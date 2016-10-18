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

}
