package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.specifications.UserSpecifications;
import com.xiaoslab.coffee.api.utility.Constants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserServiceTest extends _BaseServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void createAndGetUsers() {

        // test-case: create new user
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmailAddress("johndoe@xiaoslab.com");
        User createdUser1 = userService.create(user1);
        getLogger().info(createdUser1);

        assertNotNull(createdUser1);
        user1.setUserId(createdUser1.getUserId());
        user1.setStatus(Constants.StatusCodes.ACTIVE);
        assertEquals(user1, createdUser1);

        // test-case: create another user
        User user2 = new User();
        user2.setFirstName("Chuck");
        user2.setLastName("Norris");
        user2.setEmailAddress("chucknorris@xiaoslab.com");
        User createdUser2 = userService.create(user2);
        getLogger().info(createdUser2);

        assertNotNull(createdUser2);
        user2.setUserId(createdUser2.getUserId());
        user2.setStatus(Constants.StatusCodes.ACTIVE);
        assertEquals(user2, createdUser2);

        // test-case: list all user
        List<User> list = userService.list(Optional.empty(), Optional.empty());
        getLogger().info(list);
        assertEquals(2, list.size());
        assertEquals(createdUser1, list.get(0));
        assertEquals(createdUser2, list.get(1));

        // test-case: list all user with "johndoe@xiaoslab.com" email
        list = userService.list(Optional.of(UserSpecifications.hasEmailAddress("johndoe@xiaoslab.com")), Optional.empty());
        getLogger().info(list);
        assertEquals(1, list.size());
        assertEquals(createdUser1, list.get(0));

        // test-case: list all user with "chucknorris@xiaoslab.com" email
        list = userService.list(Optional.of(UserSpecifications.hasEmailAddress("chucknorris@xiaoslab.com")), Optional.empty());
        getLogger().info(list);
        assertEquals(1, list.size());
        assertEquals(createdUser2, list.get(0));

        // test-case: list all user with bogus email
        list = userService.list(Optional.of(UserSpecifications.hasEmailAddress("this-email-does-not-exists@xiaoslab.com")), Optional.empty());
        getLogger().info(list);
        assertEquals(0, list.size());

    }

    @Test
    public void updateUser() {

        // test-case: create and update user
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmailAddress("johndoe@xiaoslab.com");
        User createdUser = userService.create(user);
        getLogger().info(createdUser);

        assertNotNull(createdUser);
        user.setUserId(createdUser.getUserId());
        assertEquals(user, createdUser);
        assertEquals("John", createdUser.getFirstName());

        user.setFirstName("Jane");
        User updatedUser = userService.update(user);
        assertEquals(user, updatedUser);
        assertEquals("Jane", updatedUser.getFirstName());

    }

    @Test
    public void deleteUser() {

        // test-case: create two users and delete one of them
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmailAddress("johndoe@xiaoslab.com");
        User createdUser1 = userService.create(user1);
        getLogger().info(createdUser1);
        assertNotNull(createdUser1);
        assertEquals(Constants.StatusCodes.ACTIVE, createdUser1.getStatus());

        // test-case: create another user
        User user2 = new User();
        user2.setFirstName("Chuck");
        user2.setLastName("Norris");
        user2.setEmailAddress("chucknorris@xiaoslab.com");
        User createdUser2 = userService.create(user2);
        getLogger().info(createdUser2);
        assertNotNull(createdUser2);
        assertEquals(Constants.StatusCodes.ACTIVE, createdUser2.getStatus());

        User deletedUser = userService.delete(createdUser1.getUserId());
        assertEquals(Constants.StatusCodes.DELETED, deletedUser.getStatus());

        List<User> list;
        Specification<User> spec;

        // test-case: filter by active users
        spec = UserSpecifications.isActive();
        list = userService.list(Optional.of(spec), Optional.empty());
        assertEquals(1, list.size());
        assertEquals(createdUser2, list.get(0));

        // test-case: filter by email and active user
        spec = Specifications.where(UserSpecifications.hasEmailAddress("johndoe@xiaoslab.com")).and(UserSpecifications.isActive());
        list = userService.list(Optional.of(spec), Optional.empty());
        assertEquals(0, list.size());

        // test-case: no filter
        list = userService.list(Optional.empty(), Optional.empty());
        assertEquals(2, list.size());
        assertEquals(createdUser1, list.get(0));
        assertEquals(createdUser2, list.get(1));

    }
}
