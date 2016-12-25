package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.PasswordUpdateRequest;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.security.Roles;
import com.xiaoslab.coffee.api.utilities.TestConstants;
import com.xiaoslab.coffee.api.utility.Constants;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserAPITest extends _BaseAPITest {

    public static class NewUserRequest {
        public String name;
        public String emailAddress;
        public String password;
    }

    @Test
    public void registerNewUserAndLogin() throws Exception {
        NewUserRequest user = new NewUserRequest();
        user.name = "Hero Alam";
        user.emailAddress = "heroalam@xipli.com";
        user.password = TestConstants.TEST_DEFAULT_PASSWORD;
        ResponseEntity<User> response = api.registerUser(user);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // verify new user
        User newUser = response.getBody();
        assertNotNull(newUser);
        assertNotNull(newUser.getUserId());
        assertEquals("Hero", newUser.getFirstName());
        assertEquals("Alam", newUser.getLastName());
        assertEquals("Hero Alam", newUser.getName());
        assertEquals("heroalam@xipli.com", newUser.getEmailAddress());
        assertEquals(Constants.StatusCodes.PENDING, newUser.getStatus());
        assertEquals(1, newUser.getRoles().size());
        assertEquals(Roles.ROLE_USER, newUser.getRoles().stream().findFirst().get().getAuthority());

        // login as new user
        ResponseEntity loginResponse = api.login(user.emailAddress, user.password);
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
    }

    @Test
    public void createShopAdmin() throws Exception {
        api.login(XIPLI_ADMIN);
        Shop shop = apiTestUtils.createShop();
        User shopAdmin = testUtils.setupBasicUserObject(Roles.ROLE_SHOP_ADMIN);
        shopAdmin.setShopId(shop.getShopId());
        ResponseEntity<User> response = api.createUser(shopAdmin);
        User createdUser = response.getBody();
        Assert.assertEquals(shop.getShopId(), createdUser.getShopId().longValue());

        // make sure the new user can login
        testUtils.resetPassword(createdUser.getUserId());
        api.login(createdUser);
    }

    @Test
    public void passwordReset() throws Exception {
        api.login(XIPLI_ADMIN);
        Shop shop = apiTestUtils.createShop();
        User shopAdmin = testUtils.setupBasicUserObject(Roles.ROLE_SHOP_ADMIN);
        shopAdmin.setShopId(shop.getShopId());
        ResponseEntity<User> userResponse = api.createUser(shopAdmin);
        User createdUser = userResponse.getBody();
        Assert.assertEquals(shop.getShopId(), createdUser.getShopId().longValue());

        PasswordUpdateRequest request = new PasswordUpdateRequest();
        request.setEmailAddress(createdUser.getEmailAddress());
        ResponseEntity response = api.passwordReset(request);

        // TODO: validations
    }


    @Test
    public void passwordUpdate() throws Exception {

        PasswordUpdateRequest request = new PasswordUpdateRequest();
        request.setEmailAddress("testuser1480480033346@xipli.com");
        request.setCode("ag18qfp65e-k5s2lpjlb9lk9l9j424h-7v6qm69i0n");
        request.setPassword("foo");
        ResponseEntity response = api.passwordUpdate(request);

        // TODO: validations

    }

}