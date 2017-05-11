//package com.xiaoslab.coffee.api.services;
//
//import com.xiaoslab.coffee.api.objects.Item;
//import com.xiaoslab.coffee.api.objects.ItemOption;
//import com.xiaoslab.coffee.api.objects.Shop;
//import com.xiaoslab.coffee.api.utilities.ServiceLoginUtils;
//import com.xiaoslab.coffee.api.utility.Constants;
//import org.apache.log4j.Logger;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
///**
// * Created by ipeli on 11/1/16.
// */
//
//
//public class ItemOptionServiceTest extends _BaseServiceTest {
//
//    private long itemOptionidfortest;
//
//    Logger logger = Logger.getLogger(ItemServiceTest.class);
//
//    @Autowired
//    private IService<ItemOption> itemOptionService;
//
//    @Autowired
//    private IService<Item> itemService;
//
//    @Autowired
//    private IService<Shop> shopService;
//
//
//    @Autowired
//    private ServiceLoginUtils serviceLoginUtils;
//
//
//    @Test
//    public void createItemOption() {
//
//        // test-case: create new item option("Name : Small") for a item which associate with a shop named "DD"
//        ItemOption createdItemOption = preRequisiteTestScenarioForItemOption(new ItemOption("Small",BigDecimal.valueOf(3.00),Constants.StatusCodes.ACTIVE));
//        assertItemOptionName(itemOptionService.get(createdItemOption.getItemOptionId()),"Small");
//    }
//
//    @Test
//    public void updateItemOption() {
//        // test-case: create new item option("Name : Small") for a item which associate with a shop named "DD"
//        ItemOption createdItemOption = preRequisiteTestScenarioForItemOption(new ItemOption("Small",BigDecimal.valueOf(3.00),Constants.StatusCodes.ACTIVE));
//
//        // test-case: Update item option by name ("Name : Small in Size") and price  for a item which associate with a shop named "DD"
//        createdItemOption.setName("Small in size");
//        createdItemOption.setPrice(BigDecimal.valueOf(3.50));
//        ItemOption updatedItemOption = itemOptionService.update(createdItemOption);
//
//        assertItemOptionName(itemOptionService.get(createdItemOption.getItemOptionId()),"Small in size");
//    }
//
//    @Test
//    public void deleteItemOption() {
//
//        // test-case: create new item option("Name : Small") for a item which associate with a shop named "DD"
//        ItemOption createdItemOption = preRequisiteTestScenarioForItemOption(new ItemOption("Small",BigDecimal.valueOf(3.00),Constants.StatusCodes.ACTIVE));
//
//        // test-case: delete item option
//        ItemOption deletedItemOption = itemOptionService.delete(createdItemOption.getItemOptionId());
//        assertNoOfItemOption(0);
//    }
//
//    @Test
//    public void getAllItemOption() {
//
//        // test-case: create new item option for a item which associate with a shop named "DD"
//        ItemOption createdItemOption = preRequisiteTestScenarioForItemOption(new ItemOption("Small",BigDecimal.valueOf(3.00),Constants.StatusCodes.ACTIVE));
//
//        serviceLoginUtils.loginAsCustomerUser();
//        assertNoOfItemOption(1);
//    }
//
//    @Test
//    public void getItemOption() {
//        // test-case: create new item option for a item which associate with a shop named "DD"
//        ItemOption createdItemOption = preRequisiteTestScenarioForItemOption(new ItemOption("Small",BigDecimal.valueOf(3.00),Constants.StatusCodes.ACTIVE));
//
//        serviceLoginUtils.loginAsCustomerUser();
//        assertItemOptionName(itemOptionService.get(createdItemOption.getItemOptionId()),"Small");
//    }
//
//    private void assertItemOptionName(ItemOption item,String itemOptionName){
//        logger.info(item);
//        assertNotNull(item);
//        assertEquals(itemOptionName, item.getName());
//    }
//    private void assertNoOfItemOption(int noOfItemOption){
//        List<ItemOption> itemOptions = itemOptionService.list();
//        logger.info(itemOptions);
//        assertNotNull(itemOptions);
//        assertEquals(noOfItemOption, itemOptions.size());
//    }
//    private ItemOption preRequisiteTestScenarioForItemOption(ItemOption itemOption){
//
//        // test-case: create new shop and add item 1 to it
//        serviceLoginUtils.loginAsXAdmin();
//        Shop createdShop = shopService.create(testUtils.setupShopObject());
//
//        serviceLoginUtils.loginAsShopAdmin(createdShop.getShopId());
//        Item createdItem = itemService.create(testUtils.setupItemObjectForShop(createdShop.getShopId()));
//
//        // test-case: create new item option(Small) for Latte
//        itemOption.setItemId(createdItem.getItemId ());
//        return itemOptionService.create(itemOption);
//    }
//
//
//}
