package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.ItemAddon;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.utility.Constants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class ItemAddonAPITest extends _BaseAPITest {

    private Shop SHOP1;
    private User SHOP1_ADMIN;
    private User SHOP1_USER;
    private Item SHOP1_ITEM1;
    private Item SHOP1_ITEM2;

    @Before
    public void dataSetup(){

        // login as xipli admin, create test shop
        api.login(XIPLI_ADMIN);
        SHOP1 = apiDataCreator.createShopHelper();

        // login as shop admin, create test items
        SHOP1_ADMIN = apiDataCreator.createShopAdminHelper(SHOP1.getShopId());
        SHOP1_USER = apiDataCreator.createShopUserHelper(SHOP1.getShopId());
        api.login(SHOP1_ADMIN);

        SHOP1_ITEM1 = apiDataCreator.createItemHelper(SHOP1.getShopId());
        SHOP1_ITEM2 = apiDataCreator.createItemHelper(SHOP1.getShopId());

    }

    @Test
    public void createAndGetAndListItemAddons() throws Exception {
        api.login(SHOP1_ADMIN);
        ResponseEntity<List<ItemAddon>> itemAddonListResponse;
        ResponseEntity response;

        // list should be empty at the beginning
        itemAddonListResponse = api.listItemAddon(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.OK, itemAddonListResponse.getStatusCode());
        Assert.assertEquals(0, itemAddonListResponse.getBody().size());

        // create addons
        ItemAddon addon1 = new ItemAddon(SHOP1_ITEM1.getItemId(), "Sugar", BigDecimal.valueOf(3.45), Constants.StatusCodes.INACTIVE);
        ItemAddon addon2 = new ItemAddon(SHOP1_ITEM1.getItemId(), "Milk", BigDecimal.valueOf(4.56), Constants.StatusCodes.ACTIVE);
        ItemAddon addon3 = new ItemAddon(SHOP1_ITEM1.getItemId(), "Cream", BigDecimal.valueOf(5.67), Constants.StatusCodes.ACTIVE);
        itemAddonListResponse = api.updateAllItemAddons(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(addon1, addon2, addon3));
        Assert.assertEquals(HttpStatus.OK, itemAddonListResponse.getStatusCode());
        Assert.assertEquals(3, itemAddonListResponse.getBody().size());
        Assert.assertEquals(addon1, itemAddonListResponse.getBody().get(0));
        Assert.assertEquals(addon2, itemAddonListResponse.getBody().get(1));
        Assert.assertEquals(addon3, itemAddonListResponse.getBody().get(2));

        // list created addons
        itemAddonListResponse = api.listItemAddon(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.OK, itemAddonListResponse.getStatusCode());
        Assert.assertEquals(3, itemAddonListResponse.getBody().size());
        Assert.assertEquals(addon1, itemAddonListResponse.getBody().get(0));
        Assert.assertEquals(addon2, itemAddonListResponse.getBody().get(1));
        Assert.assertEquals(addon3, itemAddonListResponse.getBody().get(2));

        // remove milk addon and update price for sugar
        addon1.setPrice(BigDecimal.valueOf(3.75));
        itemAddonListResponse = api.updateAllItemAddons(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(addon1, addon3));
        Assert.assertEquals(HttpStatus.OK, itemAddonListResponse.getStatusCode());
        Assert.assertEquals(2, itemAddonListResponse.getBody().size());
        Assert.assertEquals(addon1, itemAddonListResponse.getBody().get(0));
        Assert.assertEquals(addon3, itemAddonListResponse.getBody().get(1));

        // list updated addons
        itemAddonListResponse = api.listItemAddon(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.OK, itemAddonListResponse.getStatusCode());
        Assert.assertEquals(2, itemAddonListResponse.getBody().size());
        Assert.assertEquals(addon1, itemAddonListResponse.getBody().get(0));
        Assert.assertEquals(addon3, itemAddonListResponse.getBody().get(1));

        // delete item
        response = api.deleteItem(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // list should return 404
        itemAddonListResponse = api.listItemAddon(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.NOT_FOUND, itemAddonListResponse.getStatusCode());

        // update should return 404
        itemAddonListResponse = api.updateAllItemAddons(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(addon1));
        Assert.assertEquals(HttpStatus.NOT_FOUND, itemAddonListResponse.getStatusCode());

        // list addons for the 2nd item
        itemAddonListResponse = api.listItemAddon(SHOP1.getShopId(), SHOP1_ITEM2.getItemId());
        Assert.assertEquals(HttpStatus.OK, itemAddonListResponse.getStatusCode());
        Assert.assertEquals(0, itemAddonListResponse.getBody().size());

    }

    @Test
    public void unsupportedApiEndpoints() throws Exception {
        api.login(SHOP1_ADMIN);
        ResponseEntity response;

        // create 1 item addon
        ItemAddon addon1 = new ItemAddon(SHOP1_ITEM1.getItemId(), "Sugar", BigDecimal.valueOf(3.45), Constants.StatusCodes.INACTIVE);
        response = api.updateAllItemAddons(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(addon1));
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        // try invalid endpoints
        response = api.createItemAddon(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), addon1);
        Assert.assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());

        response = api.getItemAddon(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), 1);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        response = api.updateItemAddon(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), 1, addon1);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        response = api.deleteItemAddon(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), 1);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }



    @Test
    public void createAndGetItemAddonsWithDifferentAuthorization() throws Exception {

        ResponseEntity response;
        ResponseEntity<List<ItemAddon>> itemAddonListResponse;

        api.login(SHOP1_ADMIN);

        // create 1 item addon
        ItemAddon addon1 = new ItemAddon(SHOP1_ITEM1.getItemId(), "Sugar", BigDecimal.valueOf(3.45), Constants.StatusCodes.INACTIVE);
        response = api.updateAllItemAddons(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(addon1));
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        itemAddonListResponse = api.listItemAddon(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.OK, itemAddonListResponse.getStatusCode());
        Assert.assertEquals(1, itemAddonListResponse.getBody().size());

        // as non-admin
        api.login(SHOP1_USER);
        response = api.updateAllItemAddons(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(addon1));
        Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        itemAddonListResponse = api.listItemAddon(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.OK, itemAddonListResponse.getStatusCode());
        Assert.assertEquals(1, itemAddonListResponse.getBody().size());

        // as customer user
        api.login(CUSTOMER_USER);
        response = api.updateAllItemAddons(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(addon1));
        Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        itemAddonListResponse = api.listItemAddon(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.OK, itemAddonListResponse.getStatusCode());
        Assert.assertEquals(1, itemAddonListResponse.getBody().size());

        // as xipli admin
        api.login(XIPLI_ADMIN);
        response = api.updateAllItemAddons(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(addon1));
        Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        itemAddonListResponse = api.listItemAddon(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.OK, itemAddonListResponse.getStatusCode());
        Assert.assertEquals(1, itemAddonListResponse.getBody().size());

        // without authentication
        api.logout();
        response = api.updateAllItemAddons(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(addon1));
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        itemAddonListResponse = api.listItemAddon(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, itemAddonListResponse.getStatusCode());
        Assert.assertEquals(null, itemAddonListResponse.getBody());
    }
}
