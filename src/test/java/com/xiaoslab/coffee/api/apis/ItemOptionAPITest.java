package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.*;
import com.xiaoslab.coffee.api.utility.Constants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class ItemOptionAPITest extends _BaseAPITest {

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
    public void createAndGetAndListItemOptions() throws Exception {
        api.login(SHOP1_ADMIN);
        ResponseEntity<List<ItemOption>> itemOptionListResponse;
        ResponseEntity response;

        // list should be empty at the beginning
        itemOptionListResponse = api.listItemOption(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.OK, itemOptionListResponse.getStatusCode());
        Assert.assertEquals(0, itemOptionListResponse.getBody().size());

        // create options
        ItemOption option1 = new ItemOption(SHOP1_ITEM1.getItemId(), "Small", BigDecimal.valueOf(3.45), Constants.StatusCodes.INACTIVE);
        ItemOption option2 = new ItemOption(SHOP1_ITEM1.getItemId(), "Medium", BigDecimal.valueOf(4.56), Constants.StatusCodes.ACTIVE);
        ItemOption option3 = new ItemOption(SHOP1_ITEM1.getItemId(), "Large", BigDecimal.valueOf(5.67), Constants.StatusCodes.ACTIVE);
        itemOptionListResponse = api.updateAllItemOptions(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(option1, option2, option3));
        Assert.assertEquals(HttpStatus.OK, itemOptionListResponse.getStatusCode());
        Assert.assertEquals(3, itemOptionListResponse.getBody().size());
        Assert.assertEquals(option1, itemOptionListResponse.getBody().get(0));
        Assert.assertEquals(option2, itemOptionListResponse.getBody().get(1));
        Assert.assertEquals(option3, itemOptionListResponse.getBody().get(2));

        // list created options
        itemOptionListResponse = api.listItemOption(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.OK, itemOptionListResponse.getStatusCode());
        Assert.assertEquals(3, itemOptionListResponse.getBody().size());
        Assert.assertEquals(option1, itemOptionListResponse.getBody().get(0));
        Assert.assertEquals(option2, itemOptionListResponse.getBody().get(1));
        Assert.assertEquals(option3, itemOptionListResponse.getBody().get(2));

        // remove medium option and update price for small
        option1.setPrice(BigDecimal.valueOf(3.75));
        itemOptionListResponse = api.updateAllItemOptions(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(option1, option3));
        Assert.assertEquals(HttpStatus.OK, itemOptionListResponse.getStatusCode());
        Assert.assertEquals(2, itemOptionListResponse.getBody().size());
        Assert.assertEquals(option1, itemOptionListResponse.getBody().get(0));
        Assert.assertEquals(option3, itemOptionListResponse.getBody().get(1));

        // list updated options
        itemOptionListResponse = api.listItemOption(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.OK, itemOptionListResponse.getStatusCode());
        Assert.assertEquals(2, itemOptionListResponse.getBody().size());
        Assert.assertEquals(option1, itemOptionListResponse.getBody().get(0));
        Assert.assertEquals(option3, itemOptionListResponse.getBody().get(1));

        // delete item
        response = api.deleteItem(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // list should return 404
        itemOptionListResponse = api.listItemOption(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.NOT_FOUND, itemOptionListResponse.getStatusCode());

        // update should return 404
        itemOptionListResponse = api.updateAllItemOptions(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(option1));
        Assert.assertEquals(HttpStatus.NOT_FOUND, itemOptionListResponse.getStatusCode());

        // list options for the 2nd item
        itemOptionListResponse = api.listItemOption(SHOP1.getShopId(), SHOP1_ITEM2.getItemId());
        Assert.assertEquals(HttpStatus.OK, itemOptionListResponse.getStatusCode());
        Assert.assertEquals(0, itemOptionListResponse.getBody().size());

    }

    @Test
    public void unsupportedApiEndpoints() throws Exception {
        api.login(SHOP1_ADMIN);
        ResponseEntity response;

        // create 1 item option
        ItemOption option1 = new ItemOption(SHOP1_ITEM1.getItemId(), "Small", BigDecimal.valueOf(3.45), Constants.StatusCodes.INACTIVE);
        response = api.updateAllItemOptions(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(option1));
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        // try invalid endpoints
        response = api.createItemOption(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), option1);
        Assert.assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());

        response = api.getItemOption(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), 1);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        response = api.updateItemOption(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), 1, option1);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        response = api.deleteItemOption(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), 1);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }



    @Test
    public void createAndGetItemOptionsWithDifferentAuthorization() throws Exception {

        ResponseEntity response;
        ResponseEntity<List<ItemOption>> itemOptionListResponse;

        api.login(SHOP1_ADMIN);

        // create 1 item option
        ItemOption option1 = new ItemOption(SHOP1_ITEM1.getItemId(), "Small", BigDecimal.valueOf(3.45), Constants.StatusCodes.INACTIVE);
        response = api.updateAllItemOptions(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(option1));
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        itemOptionListResponse = api.listItemOption(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.OK, itemOptionListResponse.getStatusCode());
        Assert.assertEquals(1, itemOptionListResponse.getBody().size());

        // as non-admin
        api.login(SHOP1_USER);
        response = api.updateAllItemOptions(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(option1));
        Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        itemOptionListResponse = api.listItemOption(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.OK, itemOptionListResponse.getStatusCode());
        Assert.assertEquals(1, itemOptionListResponse.getBody().size());

        // as customer user
        api.login(CUSTOMER_USER);
        response = api.updateAllItemOptions(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(option1));
        Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        itemOptionListResponse = api.listItemOption(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.OK, itemOptionListResponse.getStatusCode());
        Assert.assertEquals(1, itemOptionListResponse.getBody().size());

        // as xipli admin
        api.login(XIPLI_ADMIN);
        response = api.updateAllItemOptions(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(option1));
        Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        itemOptionListResponse = api.listItemOption(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.OK, itemOptionListResponse.getStatusCode());
        Assert.assertEquals(1, itemOptionListResponse.getBody().size());

        // without authentication
        api.logout();
        response = api.updateAllItemOptions(SHOP1.getShopId(), SHOP1_ITEM1.getItemId(), Arrays.asList(option1));
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        itemOptionListResponse = api.listItemOption(SHOP1.getShopId(), SHOP1_ITEM1.getItemId());
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, itemOptionListResponse.getStatusCode());
        Assert.assertEquals(null, itemOptionListResponse.getBody());
    }
}
