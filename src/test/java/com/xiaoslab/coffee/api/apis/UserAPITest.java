package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.User.NewUserRequest;
import com.xiaoslab.coffee.api.objects.PasswordUpdateRequest;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.security.Roles;
import com.xiaoslab.coffee.api.utilities.APIAdapter;
import com.xiaoslab.coffee.api.utilities.TestConstants;
import com.xiaoslab.coffee.api.utility.Constants;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        assertEquals(Roles.ROLE_USER, newUser.getRoles().stream().findFirst().get());

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
        Shop shop = apiDataCreator.createShopHelper();
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
        Shop shop = apiDataCreator.createShopHelper();
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
    public void shopUserLoginForDifferentShopStatus() throws Exception {
        // create new shop and user
        api.login(XIPLI_ADMIN);
        Shop shop = apiDataCreator.createShopHelper();
        User shopAdmin = testUtils.setupBasicUserObject(Roles.ROLE_SHOP_ADMIN);
        shopAdmin.setShopId(shop.getShopId());
        ResponseEntity<User> response = api.createShopUser(shop.getShopId(), shopAdmin);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User createdUser = response.getBody();

        // make sure the new user can login
        testUtils.resetPassword(createdUser.getUserId());
        ResponseEntity loginResponse = api.login(createdUser);
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());

        // suspend the shop
        api.login(XIPLI_ADMIN);
        shop.setStatus(Constants.StatusCodes.SUSPENDED);
        ResponseEntity updatedShopResponse = api.updateShop(shop.getShopId(), shop);
        assertEquals(HttpStatus.OK, updatedShopResponse.getStatusCode());

        // try to login again, should block
        try {
            api.login(createdUser);
        } catch (AssertionError error) {
            assertEquals(HttpStatus.BAD_REQUEST, APIAdapter.getLastResponse().getStatusCode());
            assertTrue(APIAdapter.getLastResponse().getBody().toString().contains("The shop this user is assigned to is either deleted or suspended"));
        }

        // make the shop inactive, user should be able to login
        api.login(XIPLI_ADMIN);
        shop.setStatus(Constants.StatusCodes.INACTIVE);
        updatedShopResponse = api.updateShop(shop.getShopId(), shop);
        assertEquals(HttpStatus.OK, updatedShopResponse.getStatusCode());
        api.login(createdUser);
        assertEquals(HttpStatus.OK, APIAdapter.getLastResponse().getStatusCode());

        // make the shop pending, user should be able to login
        api.login(XIPLI_ADMIN);
        shop.setStatus(Constants.StatusCodes.PENDING);
        updatedShopResponse = api.updateShop(shop.getShopId(), shop);
        assertEquals(HttpStatus.OK, updatedShopResponse.getStatusCode());
        api.login(createdUser);
        assertEquals(HttpStatus.OK, APIAdapter.getLastResponse().getStatusCode());

        // delete the shop and try again
        api.login(XIPLI_ADMIN);
        updatedShopResponse = api.deleteShop(shop.getShopId());
        assertEquals(HttpStatus.NO_CONTENT, updatedShopResponse.getStatusCode());
        try {
            api.login(createdUser);
        } catch (AssertionError error) {
            assertEquals(HttpStatus.BAD_REQUEST, APIAdapter.getLastResponse().getStatusCode());
            assertTrue(APIAdapter.getLastResponse().getBody().toString().contains("The shop this user is assigned to is either deleted or suspended"));
        }
    }

    @Test
    public void passwordReset() throws Exception {
        api.login(XIPLI_ADMIN);
        Shop shop = apiDataCreator.createShopHelper();
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