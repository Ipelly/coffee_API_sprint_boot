package com.xiaoslab.coffee.api.utilities;

import com.xiaoslab.coffee.api.objects.*;
import com.xiaoslab.coffee.api.repository.UserRepository;
import com.xiaoslab.coffee.api.security.Roles;
import com.xiaoslab.coffee.api.utility.Constants;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Callable;

public class TestUtils {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public static void verifyException(Callable<Void> func, Class expectedExceptionClass, String... expectedMessages) {
        try {
            func.call();
        } catch (Throwable tr) {
            Assert.assertEquals("Expected exception class did not match.", expectedExceptionClass, tr.getClass());
            for (String expectedMessage : expectedMessages) {
                Assert.assertTrue(
                        String.format("Expected message <%s> not found in actual message <%s>.", expectedMessage, tr.getMessage()),
                        tr.getMessage().contains(expectedMessage)
                );
            }
            return;
        }
        Assert.fail(String.format("Expected exception class <%s> was not thrown.", expectedExceptionClass.getSimpleName()));
    }

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
        userObject.setRoles(new ArrayList<>(Arrays.asList(roles)));
        userObject.setStatus(Constants.StatusCodes.ACTIVE);
        userObject.setProviderType(Constants.LoginProviderType.XIPLI);
        return userObject;
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
        shop.setStatus(Constants.StatusCodes.ACTIVE);
        return shop;
    }

    public Category setupCategoryObject(long shopId) {
        long time = System.currentTimeMillis();
        Category category = new Category();
        category.setName("Test Category " + time);
        category.setDescription("Just a test");
        category.setShopId(shopId);
        category.setStatus(Constants.StatusCodes.ACTIVE);
        return category;
    }

    public Item setupItemObjectForShop(long shopId) {
        Item item = new Item();
        item.setName("Latte " + System.currentTimeMillis());
        item.setDescription("Fresh brewed beans made with the milk of your choice");
        item.setPrice(new BigDecimal(3.25));
        item.setShopId(shopId);
        item.setStatus(Constants.StatusCodes.ACTIVE);
        return item;
    }

    public Item setupItemObjectForShopAndCategory(long shopId, long... categoryIds) {
        Item item = setupItemObjectForShop(shopId);
        Set<Long> categoryIdList = new HashSet<>();
        for (long categoryId : categoryIds) {
            categoryIdList.add(categoryId);
        }
        item.setCategoryIds(categoryIdList);
        return item;
    }

    public ItemOption setupItemOptionObject(long itemId) {
        ItemOption itemOption = new ItemOption();
        itemOption.setName("Small");
        itemOption.setPrice(new BigDecimal(5.2));
        itemOption.setItemId (itemId);
        itemOption.setStatus(Constants.StatusCodes.ACTIVE);
        return itemOption;
    }

    public Addon setupAddonObject(long shopId) {
        Addon addon = new Addon();
        addon.setName("Sugar");
        addon.setPrice(new BigDecimal(3.5));
        addon.setShopId (shopId);
        addon.setStatus(Constants.StatusCodes.ACTIVE);
        return addon;
    }
}
