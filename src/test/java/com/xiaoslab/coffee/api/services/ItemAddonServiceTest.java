package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.ItemAddon;
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


public class ItemAddonServiceTest extends _BaseServiceTest {

    @Autowired
    private IService<ItemAddon> itemAddonService;

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
    public void addRemoveGetItemAddons() {
        long itemId = SHOP1_ITEM1.getItemId();

        // add sugar addon
        serviceLoginUtils.loginAsShopAdmin(SHOP1.getShopId());
        ItemAddon itemAddonSugar = new ItemAddon(itemId, "Sugar", BigDecimal.valueOf(3.12), Constants.StatusCodes.ACTIVE);
        itemAddonService.updateAll(itemId, Arrays.asList(itemAddonSugar));
        List<ItemAddon> gottenAddons = itemAddonService.list(itemId);
        assertEquals(1, gottenAddons.size());
        assertContains(gottenAddons, itemAddonSugar);

        // add sugar + milk addons
        ItemAddon itemAddonMilk = new ItemAddon(itemId, "Milk", BigDecimal.valueOf(4.50), Constants.StatusCodes.ACTIVE);
        itemAddonService.updateAll(itemId, Arrays.asList(itemAddonSugar, itemAddonMilk));
        gottenAddons = itemAddonService.list(itemId);
        assertEquals(2, gottenAddons.size());
        assertContains(gottenAddons, itemAddonSugar);
        assertContains(gottenAddons, itemAddonMilk);

        // add milk new price + cream addons
        itemAddonMilk.setPrice(BigDecimal.valueOf(4.66));
        ItemAddon itemAddonCream = new ItemAddon(itemId, "Cream", BigDecimal.valueOf(5.99), Constants.StatusCodes.ACTIVE);
        itemAddonService.updateAll(itemId, Arrays.asList(itemAddonMilk, itemAddonCream));
        gottenAddons = itemAddonService.list(itemId);
        assertEquals(2, gottenAddons.size());
        assertContains(gottenAddons, itemAddonMilk);
        assertContains(gottenAddons, itemAddonCream);

        // remove all
        itemAddonService.updateAll(itemId, new ArrayList<>());
        gottenAddons = itemAddonService.list(itemId);
        assertEquals(0, gottenAddons.size());

        // add all
        itemAddonService.updateAll(itemId, Arrays.asList(itemAddonSugar, itemAddonMilk, itemAddonCream));
        gottenAddons = itemAddonService.list(itemId);
        assertEquals(3, gottenAddons.size());
        assertContains(gottenAddons, itemAddonSugar);
        assertContains(gottenAddons, itemAddonMilk);
        assertContains(gottenAddons, itemAddonCream);

        // get as customer user
        serviceLoginUtils.loginAsCustomerUser();
        gottenAddons = itemAddonService.list(itemId);
        assertEquals(3, gottenAddons.size());
        assertContains(gottenAddons, itemAddonSugar);
        assertContains(gottenAddons, itemAddonMilk);
        assertContains(gottenAddons, itemAddonCream);

        // get as shop user
        serviceLoginUtils.loginAsShopUser(SHOP1.getShopId());
        gottenAddons = itemAddonService.list(itemId);
        assertEquals(3, gottenAddons.size());
        assertContains(gottenAddons, itemAddonSugar);
        assertContains(gottenAddons, itemAddonMilk);
        assertContains(gottenAddons, itemAddonCream);

    }

    @Test
    public void sameAddonMultipleItems() {
        long itemId1 = SHOP1_ITEM1.getItemId();
        long itemId2 = SHOP1_ITEM2.getItemId();

        serviceLoginUtils.loginAsShopAdmin(SHOP1.getShopId());

        // add sugar addon for item1
        ItemAddon item1AddonSugar = new ItemAddon(itemId1, "Sugar", BigDecimal.valueOf(3.12), Constants.StatusCodes.ACTIVE);
        itemAddonService.updateAll(itemId1, Arrays.asList(item1AddonSugar));
        List<ItemAddon> gottenAddons1 = itemAddonService.list(itemId1);
        assertEquals(1, gottenAddons1.size());
        assertEquals(item1AddonSugar, gottenAddons1.get(0));

        // add sugar addon for item2 different price
        ItemAddon item2AddonSugar = new ItemAddon(itemId2, "Sugar", BigDecimal.valueOf(4.78), Constants.StatusCodes.ACTIVE);
        itemAddonService.updateAll(itemId2, Arrays.asList(item2AddonSugar));
        List<ItemAddon> gottenAddons2 = itemAddonService.list(itemId2);
        assertEquals(1, gottenAddons2.size());
        assertEquals(item2AddonSugar, gottenAddons2.get(0));

        // make sure price for item1 sugar addon did not change
        gottenAddons1 = itemAddonService.list(itemId1);
        assertEquals(1, gottenAddons1.size());
        assertEquals(item1AddonSugar, gottenAddons1.get(0));
        assertEquals(item1AddonSugar.getPrice(), gottenAddons1.get(0).getPrice());
    }

    @Test(expected = AccessDeniedException.class)
    public void updateAsRegularUser() {
        serviceLoginUtils.loginAsCustomerUser();
        ItemAddon itemAddonSugar = new ItemAddon(SHOP1_ITEM1.getItemId(), "Sugar", BigDecimal.valueOf(3.12), Constants.StatusCodes.ACTIVE);
        itemAddonService.updateAll(SHOP1_ITEM1.getItemId(), Arrays.asList(itemAddonSugar));
    }

    @Test(expected = AccessDeniedException.class)
    public void updateAsShopUser() {
        serviceLoginUtils.loginAsShopUser(SHOP1.getShopId());
        ItemAddon itemAddonSugar = new ItemAddon(SHOP1_ITEM1.getItemId(), "Sugar", BigDecimal.valueOf(3.12), Constants.StatusCodes.ACTIVE);
        itemAddonService.updateAll(SHOP1_ITEM1.getItemId(), Arrays.asList(itemAddonSugar));
    }

    @Test(expected = AccessDeniedException.class)
    public void updateAsXAdmin() {
        serviceLoginUtils.loginAsXAdmin();
        ItemAddon itemAddonSugar = new ItemAddon(SHOP1_ITEM1.getItemId(), "Sugar", BigDecimal.valueOf(3.12), Constants.StatusCodes.ACTIVE);
        itemAddonService.updateAll(SHOP1_ITEM1.getItemId(), Arrays.asList(itemAddonSugar));
    }


}
