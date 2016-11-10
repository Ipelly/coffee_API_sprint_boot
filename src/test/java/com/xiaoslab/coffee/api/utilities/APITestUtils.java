package com.xiaoslab.coffee.api.utilities;

import com.xiaoslab.coffee.api.objects.AppAuthority;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.repository.UserRepository;
import com.xiaoslab.coffee.api.security.Roles;
import com.xiaoslab.coffee.api.utility.Constants;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class APITestUtils {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    protected APIAdapter api;

    public User createXipliAdminUser() {
        User userToCreate = setupBasicUserObject(Roles.ROLE_X_ADMIN);
        return userRepository.saveAndFlush(userToCreate);
    }

    public User createCustomerUser() {
        User userToCreate = setupBasicUserObject(Roles.ROLE_USER);
        return userRepository.saveAndFlush(userToCreate);
    }

    public User createShopAdminUser(long shopId) {
        User userToCreate = setupBasicUserObject(Roles.ROLE_SHOP_ADMIN);
        userToCreate.setShopId(shopId);
        return userRepository.saveAndFlush(userToCreate);
    }

    public User resetPassword(long userId) {
        User user = userRepository.findOne(userId);
        user.setPassword(passwordEncoder.encode(TestConstants.TEST_DEFAULT_PASSWORD));
        return userRepository.saveAndFlush(user);
    }

    public User setupBasicUserObject(String... roles) {
        User userObject = new User();
        userObject.setFirstName("Test " + System.currentTimeMillis());
        userObject.setLastName("User " + System.currentTimeMillis());
        userObject.setEmailAddress("testuser" + System.currentTimeMillis() + "@xipli.com");
        userObject.setPassword(passwordEncoder.encode(TestConstants.TEST_DEFAULT_PASSWORD));
        Set<AppAuthority> authorities = new HashSet<>();
        for (String role : roles) {
            authorities.add(new AppAuthority(role));
        }
        userObject.setRoles(authorities);
        userObject.setStatus(Constants.StatusCodes.ACTIVE);
        userObject.setProviderType(Constants.LoginProviderType.XIPLI);
        return userObject;
    }

    public ResponseEntity validateCreation(ResponseEntity response) {
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        return response;
    }

    public Shop createShop() {
        ResponseEntity<Shop> response = api.createShop(setupShopObject());
        validateCreation(response);
        return response.getBody();
    }

    public Shop setupShopObject() {
        long time = System.currentTimeMillis();
        Shop shop = new Shop();
        shop.setName("Test Shop " + time);
        shop.setAddress1(time + " Xipli Ave");
        shop.setAddress2("Floor " + time);
        shop.setCity("Xiaoslab City");
        shop.setState("NY");
        shop.setZip(Long.toString(time % 100000));
        shop.setPhone("9876543210");
        shop.setLatitude(new BigDecimal(40.7426));
        shop.setLongitude(new BigDecimal(-74.0623));
        shop.setRating(5);
        return shop;
    }

}