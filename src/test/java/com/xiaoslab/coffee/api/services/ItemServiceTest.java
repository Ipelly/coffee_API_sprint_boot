package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Category;
import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.utilities.ServiceLoginUtils;
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
public class ItemServiceTest extends _BaseServiceTest {


    private long shopIdForTest, categoryIdForTest;

    Logger logger = Logger.getLogger(ItemServiceTest.class);

    @Autowired
    private IService<Item> itemService;

    @Autowired
    private IService<Shop> shopService;

    @Autowired
    private IService<Category> categoryService;

    @Autowired
    private ServiceLoginUtils serviceLoginUtils;


    @Test
    public void createItem() {
        // test-case: create new Item ("Name : Latte") for a item which associate with a shop named "DD"
        Category createdCategory = preRequisiteTestScenarioForItem();
        Item createdItem = itemService.create(new Item("Latte","Late Coffe", BigDecimal.valueOf(5.00), shopIdForTest, categoryIdForTest,Constants.StatusCodes.ACTIVE));
        assertItemName(itemService.get(createdItem.getitem_id()),"Latte");
    }

    @Test
    public void updateItem() {

        Category createdCategory = preRequisiteTestScenarioForItem();
        Item createdItem = itemService.create(new Item("Latte","Latte Coffe", BigDecimal.valueOf(5.00), shopIdForTest, categoryIdForTest,Constants.StatusCodes.ACTIVE));
        createdItem.setName("Latte C");
        Item updatedItem = itemService.update(createdItem);
        assertItemName(itemService.get(createdItem.getitem_id()),"Latte C");
    }

    @Test
    public void deleteItem() {

        Category createdCategory = preRequisiteTestScenarioForItem();
        Item createdItem = itemService.create(new Item("Latte","Latte Coffe", BigDecimal.valueOf(5.00), shopIdForTest, categoryIdForTest,Constants.StatusCodes.ACTIVE));
        Item deleteIteam = itemService.delete(createdItem.getitem_id());
        assertNoOfItem(0);
    }

    @Test
    public void getAllItem() {

        Category createdCategory = preRequisiteTestScenarioForItem();
        Item createdItem = itemService.create(new Item("Latte","Latte Coffe", BigDecimal.valueOf(5.00), shopIdForTest, categoryIdForTest,Constants.StatusCodes.ACTIVE));
        Item createdItemq = itemService.create(new Item("Esprasso","Esprasso Coffe", BigDecimal.valueOf(7.00), shopIdForTest, categoryIdForTest,Constants.StatusCodes.ACTIVE));
        serviceLoginUtils.loginAsCustomerUser();
        assertNoOfItem(2);
    }

    @Test
    public void getItem() {
        Category createdCategory = preRequisiteTestScenarioForItem();
        Item createdItem = itemService.create(new Item("Latte","Latte Coffe", BigDecimal.valueOf(5.00), shopIdForTest, categoryIdForTest,Constants.StatusCodes.ACTIVE));
        Item createdItemq = itemService.create(new Item("Esprasso","Esprasso Coffe", BigDecimal.valueOf(7.00), shopIdForTest, categoryIdForTest,Constants.StatusCodes.ACTIVE));
        serviceLoginUtils.loginAsCustomerUser();
        assertItemName(itemService.get(createdItem.getitem_id ()),"Latte");
    }

    private void assertItemName(Item Item,String ItemName){
        logger.info(Item);
        assertNotNull(Item);
        assertEquals(ItemName, Item.getName());
    }
    private void assertNoOfItem(int noOfItemOption){
        List<Item> Items = itemService.list();
        logger.info(Items);
        assertNotNull(Items);
        assertEquals(noOfItemOption, Items.size());
    }
    private Category preRequisiteTestScenarioForItem(){

        // test-case: create new shop and add item 1 to it
        serviceLoginUtils.loginAsXAdmin();
        Shop createdShop = shopService.create(testUtils.setupShopObject());
        shopIdForTest = createdShop.getShopId();

        // test-case: create new Item for Latte
        serviceLoginUtils.loginAsShopAdmin(createdShop.getShopId());
        Category createdCategory = categoryService.create(new Category("Iced","Iced Coffee", createdShop.getShopId()));
        categoryIdForTest = createdCategory.getCategory_id();
        return createdCategory;
    }
}
