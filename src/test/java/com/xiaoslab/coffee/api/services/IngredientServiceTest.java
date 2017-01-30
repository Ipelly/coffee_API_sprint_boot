package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Ingredient;
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
public class IngredientServiceTest extends _BaseServiceTest {


    private long shopidfortest;;

    Logger logger = Logger.getLogger(ItemServiceTest.class);

    @Autowired
    private IService<Ingredient> ingredientService;

    @Autowired
    private IService<Shop> shopService;


    @Autowired
    private ServiceLoginUtils serviceLoginUtils;


    @Test
    public void createIngredient() {
        // test-case: create new item ingredient("Name : wheat")
        Ingredient createdIngredient = preRequisiteTestScenarioForIngredient(new Ingredient("wheat",Constants.StatusCodes.ACTIVE));
        assertIngredientName(ingredientService.get(createdIngredient.getIngredientId()),"wheat");
    }
    @Test
    public void createIngredientByShopAdmin() {
        // test-case: create new item ingredient("Name : wheat")
        Ingredient createdIngredient = preRequisiteTestScenarioForIngredient(new Ingredient("wheat",Constants.StatusCodes.ACTIVE));

        // test-case: create new shop and add item 1 to it
        serviceLoginUtils.loginAsXAdmin();
        Shop shop = new Shop();
        shop.setName("DD");
        shop.setAddress1("165 Liberty Ave");
        shop.setAddress2(", Jersey City");
        shop.setCity("JC");
        shop.setState("NJ");
        shop.setZip("07306");
        shop.setPhone("6414517510");
        shop.setLatitude(new BigDecimal(40.7426));
        shop.setLongitude(new BigDecimal(-74.0623));
        shop.setRating(5);
        shop.setStatus(Constants.StatusCodes.ACTIVE);
        Shop createdShop = shopService.create(shop);
        shopidfortest = createdShop.getShopId();

        // Adding item1 to the shop
        serviceLoginUtils.loginAsShopAdmin(shopidfortest);

        // test-case: create new ingredient for all shops
        //Ingredient createdIngredientByShopAdmin = preRequisiteTestScenarioForIngredient(new Ingredient("wheat",Constants.StatusCodes.ACTIVE));
        Ingredient createdIngredientByShopAdmin =  ingredientService.create(preRequisiteTestScenarioForIngredient(new Ingredient("wheat",Constants.StatusCodes.ACTIVE)));

        assertIngredientName(ingredientService.get(createdIngredient.getIngredientId()),"wheat");
    }

    @Test
    public void updateIngredient() {
        // test-case: create new item ingredient("Name : wheat") for edit
        Ingredient createdIngredient = preRequisiteTestScenarioForIngredient(new Ingredient("wheat",Constants.StatusCodes.ACTIVE));

        // test-case: Update item option by name ("Name : Small in Size") and price  for a item which associate with a shop named "DD"
        createdIngredient.setName("wheat with colour");
        Ingredient updatedIngredient = ingredientService.update(createdIngredient);
        assertIngredientName(ingredientService.get(createdIngredient.getIngredientId()),"wheat with colour");
    }

    @Test
    public void deleteIngredient() {

        // test-case: create new item ingredient("Name : wheat") for delete
        Ingredient createdIngredient = preRequisiteTestScenarioForIngredient(new Ingredient("wheat",Constants.StatusCodes.ACTIVE));

        // test-case: delete item option
        Ingredient deletedIngredient = ingredientService.delete(createdIngredient.getIngredientId());
        serviceLoginUtils.loginAsCustomerUser();
        assertNoOfIngredient(0);
    }

    @Test
    public void getAllIngredient() {

        // test-case: create new item ingredient("Name : wheat")
        Ingredient createdIngredient = preRequisiteTestScenarioForIngredient(new Ingredient("wheat",Constants.StatusCodes.ACTIVE));

        serviceLoginUtils.loginAsCustomerUser();
        assertNoOfIngredient(1);
    }

    @Test
    public void getIngredient() {
        // test-case: create new item ingredient("Name : wheat")
        Ingredient createdIngredient = preRequisiteTestScenarioForIngredient(new Ingredient("wheat",Constants.StatusCodes.ACTIVE));

        serviceLoginUtils.loginAsCustomerUser();
        assertIngredientName(ingredientService.get(createdIngredient.getIngredientId()),"wheat");
    }

    private void assertIngredientName(Ingredient item,String ingredientName){
        logger.info(item);
        assertNotNull(item);
        assertEquals(ingredientName, item.getName());
    }

    private void assertNoOfIngredient(int noOfIngredient){

        List<Ingredient> ingredients = ingredientService.list();
        logger.info(ingredients);
        assertNotNull(ingredients);
        assertEquals(noOfIngredient, ingredients.size());
    }

    private Ingredient preRequisiteTestScenarioForIngredient(Ingredient ingredient){

        serviceLoginUtils.loginAsXAdmin();

        // test-case: create new ingredient for all shops
        return ingredientService.create(ingredient);
    }
}
