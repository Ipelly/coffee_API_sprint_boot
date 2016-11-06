package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.specifications.UserSpecifications;
import com.xiaoslab.coffee.api.utilities.LoginUtils;
import com.xiaoslab.coffee.api.utilities.TestConstants;
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

    @Autowired
    LoginUtils loginUtils;

    @Test
    public void createAndGetUsers() {

        // test-case: create new user
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmailAddress("johndoe@xiaoslab.com");
        user1.setPassword(TestConstants.TEST_DEFAULT_PASSWORD);
        User createdUser1 = userService.registerNewUser(user1);
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
        user2.setPassword(TestConstants.TEST_DEFAULT_PASSWORD);
        User createdUser2 = userService.registerNewUser(user2);
        getLogger().info(createdUser2);

        assertNotNull(createdUser2);
        user2.setUserId(createdUser2.getUserId());
        user2.setStatus(Constants.StatusCodes.ACTIVE);
        assertEquals(user2, createdUser2);

        loginUtils.loginAsXAdmin();
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
        user.setPassword(TestConstants.TEST_DEFAULT_PASSWORD);
        User createdUser = userService.registerNewUser(user);
        getLogger().info(createdUser);

        assertNotNull(createdUser);
        user.setUserId(createdUser.getUserId());
        assertEquals(user, createdUser);
        assertEquals("John", createdUser.getFirstName());

        loginUtils.loginAsXAdmin();
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
        user1.setPassword(TestConstants.TEST_DEFAULT_PASSWORD);
        User createdUser1 = userService.registerNewUser(user1);
        getLogger().info(createdUser1);
        assertNotNull(createdUser1);
        assertEquals(Constants.StatusCodes.PENDING, createdUser1.getStatus());

        // test-case: create another user
        User user2 = new User();
        user2.setFirstName("Chuck");
        user2.setLastName("Norris");
        user2.setEmailAddress("chucknorris@xiaoslab.com");
        user2.setPassword(TestConstants.TEST_DEFAULT_PASSWORD);
        User createdUser2 = userService.registerNewUser(user2);
        getLogger().info(createdUser2);
        assertNotNull(createdUser2);
        assertEquals(Constants.StatusCodes.PENDING, createdUser2.getStatus());

        User deletedUser = userService.delete(createdUser1.getUserId());
        assertEquals(Constants.StatusCodes.DELETED, deletedUser.getStatus());

        List<User> list;
        Specification<User> spec;
        loginUtils.loginAsXAdmin();

        // test-case: filter by non-deleted users
        spec = UserSpecifications.isNotDeleted();
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
