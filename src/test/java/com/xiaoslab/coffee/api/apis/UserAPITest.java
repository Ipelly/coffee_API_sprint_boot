package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.User.NewUserRequest;
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

    @Test
    public void registerNewUserAndLogin() throws Exception {
        NewUserRequest user = new NewUserRequest();
        user.setName("Hero Alam");
        user.setEmailAddress("heroalam@xipli.com");
        user.setPassword(TestConstants.TEST_DEFAULT_PASSWORD);
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
        ResponseEntity loginResponse = api.login(user);
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
    }

    @Test
    public void createShopUserByCallingUserEndpoint() throws Exception {
        api.login(XIPLI_ADMIN);
        User shopAdmin = testUtils.setupBasicUserObject(Roles.ROLE_SHOP_ADMIN);
        ResponseEntity<User> response = api.createUser(shopAdmin);
        Assert.assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }

    @Test
    public void createShopAdminAndLogin() throws Exception {
        api.login(XIPLI_ADMIN);
        Shop shop = apiTestUtils.createShop();
        User shopAdmin = testUtils.setupBasicUserObject(Roles.ROLE_SHOP_ADMIN);
        shopAdmin.setShopId(shop.getShopId());
        ResponseEntity<User> response = api.createShopUser(shop.getShopId(), shopAdmin);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User createdUser = response.getBody();
        Assert.assertEquals(shop.getShopId(), createdUser.getShopId().longValue());

        // make sure the new user can login
        testUtils.resetPassword(createdUser.getUserId());
        ResponseEntity loginResponse = api.login(createdUser);
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
    }

    @Test
    public void createShopAdminWithPassword() throws Exception {
        api.login(XIPLI_ADMIN);
        Shop shop = apiTestUtils.createShop();
        NewUserRequest shopAdmin = new NewUserRequest();
        shopAdmin.setFirstName("Shop");
        shopAdmin.setLastName("Admin");
        shopAdmin.setEmailAddress("admin@xiplishop.com");
        shopAdmin.setPassword(TestConstants.TEST_DEFAULT_PASSWORD);

        ResponseEntity<User> response = api.createShopUser(shop.getShopId(), shopAdmin);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User createdUser = response.getBody();
        Assert.assertEquals(shop.getShopId(), createdUser.getShopId().longValue());

        // make sure the new user can login
        ResponseEntity loginResponse = api.login(createdUser);
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
    }

    @Test
    public void passwordReset() throws Exception {
        api.login(XIPLI_ADMIN);
        Shop shop = apiTestUtils.createShop();
        User shopAdmin = testUtils.setupBasicUserObject(Roles.ROLE_SHOP_ADMIN);
        ResponseEntity<User> userResponse = api.createShopUser(shop.getShopId(), shopAdmin);
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