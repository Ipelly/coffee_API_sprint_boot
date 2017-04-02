package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Category;
import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.utilities.ServiceLoginUtils;
import com.xiaoslab.coffee.api.utility.Constants;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by ipeli on 11/1/17.
 */

public class ItemServiceTest extends _BaseServiceTest {

    private static Shop SHOP1;
    private static Shop SHOP2;
    private static Category SHOP1_CATEGORY1;
    private static Category SHOP1_CATEGORY2;
    private static Category SHOP1_CATEGORY3;
    private static Category SHOP2_CATEGORY1;
    private static Category SHOP2_CATEGORY2;

    @Before
    public void dataSetup(){
        serviceLoginUtils.loginAsXAdmin();
        SHOP1 = shopService.create(testUtils.setupShopObject());
        SHOP2 = shopService.create(testUtils.setupShopObject());

        serviceLoginUtils.loginAsShopAdmin(SHOP1);
        SHOP1_CATEGORY1 = categoryService.create(new Category("Iced Drinks","Iced Drinks for Shop1", SHOP1.getShopId()));
        SHOP1_CATEGORY2 = categoryService.create(new Category("Hot Drinks","Hot Drinks for Shop1", SHOP1.getShopId()));
        SHOP1_CATEGORY3 = categoryService.create(new Category("Smoothies","Smoothies for Shop1", SHOP1.getShopId()));

        serviceLoginUtils.loginAsShopAdmin(SHOP2);
        SHOP2_CATEGORY1 = categoryService.create(new Category("Iced Drinks","Iced Drinks for Shop2", SHOP2.getShopId()));
        SHOP2_CATEGORY2 = categoryService.create(new Category("Hot Drinks","Hot Drinks for Shop2", SHOP2.getShopId()));
    }

    @Test
    public void createItemWithCategories() {
        serviceLoginUtils.loginAsShopAdmin(SHOP1);

        Item item = testUtils.setupItemObjectForShopAndCategory(
                SHOP1.getShopId(),
                SHOP1_CATEGORY1.getCategoryId(),
                SHOP1_CATEGORY2.getCategoryId()
        );

        // create the item
        Item createdItem = itemService.create(item);
        getLogger().info(createdItem);
        assertEquals(2, createdItem.getCategoryIds().size());
        assertEquals(new HashSet<>(Arrays.asList(SHOP1_CATEGORY1.getCategoryId(), SHOP1_CATEGORY2.getCategoryId())), createdItem.getCategoryIds());
        assertEquals(SHOP1.getShopId(), createdItem.getShopId());
        assertEquals(item.getName(), createdItem.getName());
        assertEquals(item.getDescription(), createdItem.getDescription());
        assertEquals(item.getPrice(), createdItem.getPrice());
        assertEquals(item.getStatus(), createdItem.getStatus());

        // get the item back
        Item gottenItem = itemService.get(item.getItemId());
        getLogger().info(gottenItem);
        assertEquals(2, gottenItem.getCategoryIds().size());
        assertEquals(new HashSet<>(Arrays.asList(SHOP1_CATEGORY1.getCategoryId(), SHOP1_CATEGORY2.getCategoryId())), createdItem.getCategoryIds());
        assertEquals(SHOP1.getShopId(), gottenItem.getShopId());
        assertEquals(item.getName(), gottenItem.getName());
        assertEquals(item.getDescription(), gottenItem.getDescription());
        assertEquals(item.getPrice(), gottenItem.getPrice());
        assertEquals(item.getStatus(), gottenItem.getStatus());
    }

    @Test
    public void createItemWithNoCategories() {
        serviceLoginUtils.loginAsShopAdmin(SHOP1);

        Item item = testUtils.setupItemObjectForShop(SHOP1.getShopId());
        Item createdItem;

        // create the item with null category
        item.setCategoryIds(null);
        createdItem = itemService.create(item);
        getLogger().info(createdItem);
        assertEquals(new HashSet<>(), createdItem.getCategoryIds());


        // create the item with empty category
        item.setCategoryIds(new HashSet<>());
        createdItem = itemService.create(item);
        getLogger().info(createdItem);
        assertEquals(new HashSet<>(), createdItem.getCategoryIds());
    }


    @Test
    public void createItemWithCategoryFromAnotherShop() {
        serviceLoginUtils.loginAsShopAdmin(SHOP1);

        Item item = testUtils.setupItemObjectForShopAndCategory(SHOP1.getShopId(), SHOP2_CATEGORY1.getCategoryId());

        // create the item
        try {
            itemService.create(item);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ex) {
            // expected
            assertTrue("Expected error message", ex.getMessage().contains(SHOP2_CATEGORY1.getCategoryId() + " is not a valid categoryId"));
        }
    }

    @Test
    public void createItemUnderAnotherShop() {
        serviceLoginUtils.loginAsShopAdmin(SHOP1);

        Item item = testUtils.setupItemObjectForShop(SHOP1.getShopId());
        item.setShopId(SHOP2.getShopId());

        // create the item
        try {
            itemService.create(item);
            fail("AccessDeniedException expected");
        } catch (AccessDeniedException ex) {
            // expected
            getLogger().info(ex);
            assertTrue("Expected error message", ex.getMessage().contains("User not allowed to access shop"));
        }
    }

    @Test
    public void updateItem() {
        serviceLoginUtils.loginAsShopAdmin(SHOP1);

        Item item = testUtils.setupItemObjectForShopAndCategory(
                SHOP1.getShopId(),
                SHOP1_CATEGORY1.getCategoryId(),
                SHOP1_CATEGORY2.getCategoryId()
        );

        // create the item
        Item createdItem = itemService.create(item);

        // update the item
        createdItem.setName("Cappuccino " + System.currentTimeMillis());
        createdItem.setDescription("updated description");
        createdItem.setPrice(new BigDecimal(4.5));
        createdItem.setStatus(Constants.StatusCodes.INACTIVE);
        createdItem.setCategoryIds(new HashSet<>(Arrays.asList(SHOP1_CATEGORY2.getCategoryId(), SHOP1_CATEGORY3.getCategoryId())));
        Item updatedItem = itemService.update(createdItem);
        assertEquals(createdItem.getName(), updatedItem.getName());
        assertEquals(createdItem.getDescription(), updatedItem.getDescription());
        assertEquals(createdItem.getStatus(), updatedItem.getStatus());
        assertEquals(createdItem.getPrice(), updatedItem.getPrice());
        assertEquals(2, updatedItem.getCategoryIds().size());
        assertEquals(new HashSet<>(Arrays.asList(SHOP1_CATEGORY2.getCategoryId(), SHOP1_CATEGORY3.getCategoryId())), createdItem.getCategoryIds());
    }

    @Test
    public void updateItemWithAnotherShopsCategory() {
        serviceLoginUtils.loginAsShopAdmin(SHOP1);
        Item item = testUtils.setupItemObjectForShopAndCategory(SHOP1.getShopId(), SHOP1_CATEGORY1.getCategoryId());

        // create the item
        Item createdItem = itemService.create(item);

        // update the item
        createdItem.setCategoryIds(new HashSet<>(Arrays.asList(SHOP2_CATEGORY2.getCategoryId())));
        try {
            Item updatedItem = itemService.update(createdItem);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ex) {
            // expected
            assertTrue("Expected error message", ex.getMessage().contains(SHOP2_CATEGORY2.getCategoryId() + " is not a valid categoryId"));
        }
    }

    @Test
    public void addAndRemoveCategoryAndItemBidirectional() {

        serviceLoginUtils.loginAsShopAdmin(SHOP1);

        // add items to some categories
        Item itemA = testUtils.setupItemObjectForShopAndCategory(SHOP1.getShopId(), SHOP1_CATEGORY1.getCategoryId(), SHOP1_CATEGORY2.getCategoryId());
        Item createdItemA = itemService.create(itemA);
        Item gottenItemA = itemService.get(createdItemA.getItemId());
        getLogger().info(gottenItemA);
        assertCategoriesForItem(gottenItemA, SHOP1_CATEGORY1, SHOP1_CATEGORY2);

        Item itemB = testUtils.setupItemObjectForShopAndCategory(SHOP1.getShopId(), SHOP1_CATEGORY2.getCategoryId(), SHOP1_CATEGORY3.getCategoryId());
        Item createdItemB = itemService.create(itemB);
        Item gottenItemB = itemService.get(createdItemB.getItemId());
        getLogger().info(gottenItemB);
        assertCategoriesForItem(gottenItemB, SHOP1_CATEGORY2, SHOP1_CATEGORY3);

        refreshEntities();

        // check items on category side
        Category gottenCategory1 = categoryService.get(SHOP1_CATEGORY1.getCategoryId());
        getLogger().info(gottenCategory1);
        assertItemsForCategory(gottenCategory1, gottenItemA);

        Category gottenCategory2 = categoryService.get(SHOP1_CATEGORY2.getCategoryId());
        getLogger().info(gottenCategory2);
        assertItemsForCategory(gottenCategory2, gottenItemA, gottenItemB);

        Category gottenCategory3 = categoryService.get(SHOP1_CATEGORY3.getCategoryId());
        getLogger().info(gottenCategory3);
        assertItemsForCategory(gottenCategory3, gottenItemB);

        // now update items
        // item-A --> remove category-2
        // item-A --> add category-3
        gottenItemA.setCategoryIds(new HashSet<>(Arrays.asList(SHOP1_CATEGORY1.getCategoryId(), SHOP1_CATEGORY3.getCategoryId())));
        Item updatedItemA = itemService.update(gottenItemA);
        Item afterUpdateItemA = itemService.get(gottenItemA.getItemId());
        assertCategoriesForItem(afterUpdateItemA, SHOP1_CATEGORY1, SHOP1_CATEGORY3);
        Item afterUpdateItemB = itemService.get(gottenItemB.getItemId());
        assertCategoriesForItem(afterUpdateItemB, SHOP1_CATEGORY2, SHOP1_CATEGORY3);

        refreshEntities();

        // now check the items again on category side
        gottenCategory1 = categoryService.get(SHOP1_CATEGORY1.getCategoryId());
        getLogger().info(gottenCategory1);
        assertItemsForCategory(gottenCategory1, gottenItemA);

        gottenCategory2 = categoryService.get(SHOP1_CATEGORY2.getCategoryId());
        getLogger().info(gottenCategory2);
        assertItemsForCategory(gottenCategory2, gottenItemB);

        gottenCategory3 = categoryService.get(SHOP1_CATEGORY3.getCategoryId());
        getLogger().info(gottenCategory3);
        assertItemsForCategory(gottenCategory3, gottenItemA, gottenItemB);

        // now update a category
        // category-3 --> remove ItemB
        gottenCategory3.setItemIds(new HashSet<>(Arrays.asList(gottenItemA.getItemId())));
        Category afterUpdateCategory3 = categoryService.update(gottenCategory3);
        assertItemsForCategory(afterUpdateCategory3, gottenItemA);

        refreshEntities();

        // now check the category on item side
        afterUpdateItemA = itemService.get(gottenItemA.getItemId());
        assertCategoriesForItem(afterUpdateItemA, SHOP1_CATEGORY1, SHOP1_CATEGORY3);
        afterUpdateItemB = itemService.get(gottenItemB.getItemId());
        assertCategoriesForItem(afterUpdateItemB, SHOP1_CATEGORY2);

        // delete an item and check category
        itemService.delete(gottenItemB.getItemId());
        refreshEntities();
        Category afterUpdateCategory2 = categoryService.get(gottenCategory2.getCategoryId());
        assertItemsForCategory(afterUpdateCategory2);

        // delete a category and check item
        categoryService.delete(gottenCategory1.getCategoryId());
        refreshEntities();
        afterUpdateItemA = itemService.get(gottenItemA.getItemId());
        assertCategoriesForItem(afterUpdateItemA, SHOP1_CATEGORY3);

    }

    @Test
    public void deleteItem() {
        serviceLoginUtils.loginAsShopAdmin(SHOP1);
        Item item = testUtils.setupItemObjectForShopAndCategory(SHOP1.getShopId(), SHOP1_CATEGORY1.getCategoryId());
        Item createdItem = itemService.create(item);
        itemService.delete(createdItem.getItemId());
        assertNull(itemService.get(createdItem.getItemId()));
        assertNoOfItemsForCustomer(0);
        assertNoOfItemsForShop(0, SHOP1.getShopId());
    }

    @Test
    public void deleteItemFromAnotherShop() {
        serviceLoginUtils.loginAsShopAdmin(SHOP1);
        Item item = testUtils.setupItemObjectForShopAndCategory(SHOP1.getShopId(), SHOP1_CATEGORY1.getCategoryId());
        Item createdItem = itemService.create(item);

        serviceLoginUtils.loginAsShopAdmin(SHOP2);
        try {
            itemService.delete(createdItem.getItemId());
            fail("AccessDeniedException expected");
        } catch (AccessDeniedException ex) {
            // expected
            assertTrue("Error message", ex.getMessage().contains("User not allowed to access shop"));
        }
    }

    @Test
    public void listItems() {
        serviceLoginUtils.loginAsShopAdmin(SHOP1);
        Item item1 = testUtils.setupItemObjectForShopAndCategory(SHOP1.getShopId(), SHOP1_CATEGORY1.getCategoryId());
        Item createdItem1 = itemService.create(item1);

        Item item2 = testUtils.setupItemObjectForShopAndCategory(SHOP1.getShopId(), SHOP1_CATEGORY1.getCategoryId());
        Item createdItem2 = itemService.create(item2);

        assertNoOfItemsForCustomer(2);
        assertNoOfItemsForShop(2, SHOP1.getShopId());
        assertNoOfItemsForShop(0, SHOP2.getShopId());

        serviceLoginUtils.loginAsShopAdmin(SHOP2);
        Item item3 = testUtils.setupItemObjectForShopAndCategory(SHOP2.getShopId(), SHOP2_CATEGORY1.getCategoryId());
        Item createdItem3 = itemService.create(item3);

        assertNoOfItemsForCustomer(3);
        assertNoOfItemsForShop(2, SHOP1.getShopId());
        assertNoOfItemsForShop(1, SHOP2.getShopId());
    }

    @Test
    public void getItemFromAnotherShop() {
        serviceLoginUtils.loginAsShopAdmin(SHOP1);
        Item item = testUtils.setupItemObjectForShopAndCategory(SHOP1.getShopId(), SHOP1_CATEGORY1.getCategoryId());
        Item createdItem = itemService.create(item);

        Item gottenItem;

        serviceLoginUtils.loginAsShopAdmin(SHOP1);
        gottenItem = itemService.get(createdItem.getItemId());
        assertNotNull(gottenItem);

        serviceLoginUtils.loginAsShopAdmin(SHOP2);
        gottenItem = itemService.get(createdItem.getItemId());
        assertNull(gottenItem);

        serviceLoginUtils.loginAsCustomerUser();
        gottenItem = itemService.get(createdItem.getItemId());
        assertNotNull(gottenItem);
    }

    private void assertNoOfItemsForCustomer(int noOfItems){
        serviceLoginUtils.loginAsCustomerUser();
        List<Item> Items = itemService.list();
        getLogger().info(Items);
        assertNotNull(Items);
        assertEquals(noOfItems, Items.size());
    }

    private void assertNoOfItemsForShop(int noOfItems, long shopId){
        serviceLoginUtils.loginAsShopAdmin(shopId);
        List<Item> Items = itemService.list();
        getLogger().info(Items);
        assertNotNull(Items);
        assertEquals(noOfItems, Items.size());
    }

    private void assertCategoriesForItem(Item item, Category... categories){
        Set<Long> expectedCategoryIds = new HashSet<>();
        for (Category category : categories) {
            expectedCategoryIds.add(category.getCategoryId());
        }
        assertEquals(expectedCategoryIds, item.getCategoryIds());
    }

    private void assertItemsForCategory(Category category, Item... items){
        Set<Long> expectedItemIds = new HashSet<>();
        for (Item item : items) {
            expectedItemIds.add(item.getItemId());
        }
        assertEquals(expectedItemIds, category.getItemIds());
    }
}
