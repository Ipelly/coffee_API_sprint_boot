package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Addon;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.utilities.LoginUtils;
import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by ipeli on 11/1/16.
 */


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddonServiceTest extends _BaseServiceTest {


    private long itemOptionidfortest;

    Logger logger = Logger.getLogger(AddonServiceTest.class);

    @Autowired
    private IService<Addon> addonService;

    @Autowired
    private IService<Shop> shopService;


    @Autowired
    private LoginUtils loginUtils;


    @Test
    public void createAddon() {
        // test-case: create new Addon ("Name : Extra Sugar") for a item which associate with a shop named "DD"
        Addon createdAddon = preRequisiteTestScenarioForAddon(new Addon("Extra Sugar",BigDecimal.valueOf(1.00),Constants.StatusCodes.ACTIVE));
        assertAddonName(addonService.get(createdAddon.getAddon_id()),"Extra Sugar");
    }

    @Test
    public void updateAddon() {
        // test-case: create new Addon ("Name : Extra Sugar") for a item which associate with a shop named "DD"
        Addon createdAddon = preRequisiteTestScenarioForAddon(new Addon("Extra Sugar",BigDecimal.valueOf(1.00),Constants.StatusCodes.ACTIVE));

        // test-case: Update Addon by name ("Name : Extra Sugar X") and price  for a item which associate with a shop named "DD"
        createdAddon.setName("Extra Sugar X");
        createdAddon.setPrice(BigDecimal.valueOf(1.00));
        Addon updatedAddon = addonService.update(createdAddon);

        assertAddonName(addonService.get(updatedAddon.getAddon_id ()),"Extra Sugar X");
    }

    @Test
    public void deleteAddon() {

        // test-case: create new Addon ("Name : Extra Sugar") for a item which associate with a shop named "DD"
        Addon createdAddon = preRequisiteTestScenarioForAddon(new Addon("Extra Sugar",BigDecimal.valueOf(1.00),Constants.StatusCodes.ACTIVE));

        // test-case: delete item option
        Addon deletedAddon = addonService.delete(createdAddon.getAddon_id());
        assertNoOfAddon(0);
    }

    @Test
    public void getAllAddon() {

        // test-case: create new Addon ("Name : Extra Sugar") for a item which associate with a shop named "DD"
        Addon createdAddon = preRequisiteTestScenarioForAddon(new Addon("Extra Sugar",BigDecimal.valueOf(1.00),Constants.StatusCodes.ACTIVE));

        loginUtils.loginAsCustomerUser();
        assertNoOfAddon(1);
    }

    @Test
    public void getAddon() {
        // test-case: create new item option for a item which associate with a shop named "DD"
        Addon createdAddon = preRequisiteTestScenarioForAddon(new Addon("Extra Sugar",BigDecimal.valueOf(1.00),Constants.StatusCodes.ACTIVE));

        loginUtils.loginAsCustomerUser();
        assertAddonName(addonService.get(createdAddon.getAddon_id ()),"Extra Sugar");
    }

    private void assertAddonName(Addon addon,String addonName){
        logger.info(addon);
        assertNotNull(addon);
        assertEquals(addonName, addon.getName());
    }

    private void assertNoOfAddon(int noOfItemOption){
        List<Addon> addons = addonService.list();
        logger.info(addons);
        assertNotNull(addons);
        assertEquals(noOfItemOption, addons.size());
    }
    private Addon preRequisiteTestScenarioForAddon(Addon addon){

        // test-case: create new shop and add item 1 to it
        loginUtils.loginAsXAdmin();
        Shop createdShop = shopService.create(testUtils.setupShopObject());

        // test-case: create new Addon for Latte
        loginUtils.loginAsShopAdmin(createdShop.getShopId());
        addon.setShop_id(createdShop.getShopId ());
        return addonService.create(addon);
    }
}
