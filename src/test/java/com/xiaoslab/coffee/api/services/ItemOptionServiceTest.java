package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.ItemOption;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.utilities.ServiceLoginUtils;
import com.xiaoslab.coffee.api.utility.Constants;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class ItemOptionServiceTest extends _BaseServiceTest {

    @Autowired
    private IService<ItemOption> itemOptionService;

    @Autowired
    private IService<Item> itemService;

    @Autowired
    private IService<Shop> shopService;

    @Autowired
    private ServiceLoginUtils serviceLoginUtils;

    private static Shop SHOP1;
    private static Item SHOP1_ITEM1;
    private static Item SHOP1_ITEM2;

    @Before
    public void dataSetup(){

        serviceLoginUtils.loginAsXAdmin();
        SHOP1 = shopService.create(testUtils.setupShopObject());

        serviceLoginUtils.loginAsShopAdmin(SHOP1.getShopId());
        SHOP1_ITEM1 = itemService.create(testUtils.setupItemObjectForShop(SHOP1.getShopId()));
        SHOP1_ITEM2 = itemService.create(testUtils.setupItemObjectForShop(SHOP1.getShopId()));
    }

    @Test
    public void addRemoveGetItemOptions() {
        long itemId = SHOP1_ITEM1.getItemId();

        // add small option
        serviceLoginUtils.loginAsShopAdmin(SHOP1.getShopId());
        ItemOption itemOptionSmall = new ItemOption(itemId, "Small", BigDecimal.valueOf(3.12), Constants.StatusCodes.ACTIVE);
        itemOptionService.updateAll(itemId, Arrays.asList(itemOptionSmall));
        List<ItemOption> gottenOptions = itemOptionService.list(itemId);
        assertEquals(1, gottenOptions.size());
        assertEquals(itemOptionSmall, gottenOptions.get(0));

        // add small + medium options
        ItemOption itemOptionMedium = new ItemOption(itemId, "Medium", BigDecimal.valueOf(4.50), Constants.StatusCodes.ACTIVE);
        itemOptionService.updateAll(itemId, Arrays.asList(itemOptionSmall, itemOptionMedium));
        gottenOptions = itemOptionService.list(itemId);
        assertEquals(2, gottenOptions.size());
        assertEquals(itemOptionSmall, gottenOptions.get(0));
        assertEquals(itemOptionMedium, gottenOptions.get(1));

        // add medium new price + large options
        itemOptionMedium.setPrice(BigDecimal.valueOf(4.66));
        ItemOption itemOptionLarge = new ItemOption(itemId, "Large", BigDecimal.valueOf(5.99), Constants.StatusCodes.ACTIVE);
        itemOptionService.updateAll(itemId, Arrays.asList(itemOptionMedium, itemOptionLarge));
        gottenOptions = itemOptionService.list(itemId);
        assertEquals(2, gottenOptions.size());
        assertEquals(itemOptionMedium, gottenOptions.get(0));
        assertEquals(itemOptionLarge, gottenOptions.get(1));

        // remove all
        itemOptionService.updateAll(itemId, new ArrayList<>());
        gottenOptions = itemOptionService.list(itemId);
        assertEquals(0, gottenOptions.size());

        // add all
        itemOptionService.updateAll(itemId, Arrays.asList(itemOptionSmall, itemOptionMedium, itemOptionLarge));
        gottenOptions = itemOptionService.list(itemId);
        assertEquals(3, gottenOptions.size());
        assertEquals(itemOptionSmall, gottenOptions.get(0));
        assertEquals(itemOptionMedium, gottenOptions.get(1));
        assertEquals(itemOptionLarge, gottenOptions.get(2));

        // get as customer user
        serviceLoginUtils.loginAsCustomerUser();
        gottenOptions = itemOptionService.list(itemId);
        assertEquals(3, gottenOptions.size());
        assertEquals(itemOptionSmall, gottenOptions.get(0));
        assertEquals(itemOptionMedium, gottenOptions.get(1));
        assertEquals(itemOptionLarge, gottenOptions.get(2));

        // get as shop user
        serviceLoginUtils.loginAsShopUser(SHOP1.getShopId());
        gottenOptions = itemOptionService.list(itemId);
        assertEquals(3, gottenOptions.size());
        assertEquals(itemOptionSmall, gottenOptions.get(0));
        assertEquals(itemOptionMedium, gottenOptions.get(1));
        assertEquals(itemOptionLarge, gottenOptions.get(2));

    }

    @Test
    public void sameOptionMultipleItems() {
        long itemId1 = SHOP1_ITEM1.getItemId();
        long itemId2 = SHOP1_ITEM2.getItemId();

        serviceLoginUtils.loginAsShopAdmin(SHOP1.getShopId());

        // add small option for item1
        ItemOption item1OptionSmall = new ItemOption(itemId1, "Small", BigDecimal.valueOf(3.12), Constants.StatusCodes.ACTIVE);
        itemOptionService.updateAll(itemId1, Arrays.asList(item1OptionSmall));
        List<ItemOption> gottenOptions1 = itemOptionService.list(itemId1);
        assertEquals(1, gottenOptions1.size());
        assertEquals(item1OptionSmall, gottenOptions1.get(0));

        // add small option for item2 different price
        ItemOption item2OptionSmall = new ItemOption(itemId2, "Small", BigDecimal.valueOf(4.78), Constants.StatusCodes.ACTIVE);
        itemOptionService.updateAll(itemId2, Arrays.asList(item2OptionSmall));
        List<ItemOption> gottenOptions2 = itemOptionService.list(itemId2);
        assertEquals(1, gottenOptions2.size());
        assertEquals(item2OptionSmall, gottenOptions2.get(0));

        // make sure price for item1 small option did not change
        gottenOptions1 = itemOptionService.list(itemId1);
        assertEquals(1, gottenOptions1.size());
        assertEquals(item1OptionSmall, gottenOptions1.get(0));
        assertEquals(item1OptionSmall.getPrice(), gottenOptions1.get(0).getPrice());
    }

    @Test(expected = AccessDeniedException.class)
    public void updateAsRegularUser() {
        serviceLoginUtils.loginAsCustomerUser();
        ItemOption itemOptionSmall = new ItemOption(SHOP1_ITEM1.getItemId(), "Small", BigDecimal.valueOf(3.12), Constants.StatusCodes.ACTIVE);
        itemOptionService.updateAll(SHOP1_ITEM1.getItemId(), Arrays.asList(itemOptionSmall));
    }

    @Test(expected = AccessDeniedException.class)
    public void updateAsShopUser() {
        serviceLoginUtils.loginAsShopUser(SHOP1.getShopId());
        ItemOption itemOptionSmall = new ItemOption(SHOP1_ITEM1.getItemId(), "Small", BigDecimal.valueOf(3.12), Constants.StatusCodes.ACTIVE);
        itemOptionService.updateAll(SHOP1_ITEM1.getItemId(), Arrays.asList(itemOptionSmall));
    }

    @Test(expected = AccessDeniedException.class)
    public void updateAsXAdmin() {
        serviceLoginUtils.loginAsXAdmin();
        ItemOption itemOptionSmall = new ItemOption(SHOP1_ITEM1.getItemId(), "Small", BigDecimal.valueOf(3.12), Constants.StatusCodes.ACTIVE);
        itemOptionService.updateAll(SHOP1_ITEM1.getItemId(), Arrays.asList(itemOptionSmall));
    }


}
