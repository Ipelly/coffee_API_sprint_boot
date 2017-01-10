package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.Ingredient;
import com.xiaoslab.coffee.api.utility.Constants;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by ipeli on 10/19/16.
 */
public class IngredientAPITest extends _BaseAPITest {

    @Test
    public void createAndGetAndListIngredient() throws Exception {

        api.login(XIPLI_ADMIN);

        ResponseEntity<List<Ingredient>> listResponse;
        ResponseEntity<Ingredient> response;

        // test-case: create new Ingredient 1 by POST
        Ingredient ingredient = new Ingredient("wheat",Constants.StatusCodes.ACTIVE);
        response = api.createIngredient(ingredient);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Ingredient createdIngredient = response.getBody();
        assertNotNull(createdIngredient);
        assertTrue(createdIngredient.getIngredientId() > 0);
        ingredient.setIngredientId(createdIngredient.getIngredientId());
        assertEquals(ingredient, createdIngredient);

        // test-case: create new Ingredient 1 by POST
        Ingredient ingredient1 = new Ingredient("wheat",Constants.StatusCodes.ACTIVE);
        response = api.createIngredient(ingredient1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Ingredient createdIngredient1 = response.getBody();
        assertNotNull(createdIngredient1);
        assertTrue(createdIngredient1.getIngredientId() > 0);
        ingredient1.setIngredientId(createdIngredient1.getIngredientId());
        assertEquals(ingredient1, createdIngredient1);



        // test-case: list of ingredient by GET
        api.login(CUSTOMER_USER);

        listResponse = api.listIngredient();
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        List<Ingredient> ingredientList = listResponse.getBody();
        assertEquals(2, ingredientList.size());
        assertEquals(createdIngredient, ingredientList.get(0));
        assertEquals(createdIngredient1, ingredientList.get(1));

        // test-case: get individual ingredient by GET
        response = api.getIngredient(createdIngredient.getIngredientId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Ingredient gottenIngredient = response.getBody();
        assertEquals(createdIngredient, gottenIngredient);

    }

    @Test
    public void createIngredientWithoutAuthorization() throws Exception {

        ResponseEntity<List<Ingredient>> listResponse;
        ResponseEntity<Ingredient> response;

        // test-case: create new Ingredient 1 by POST by X Admin
        Ingredient ingredient = new Ingredient("wheat",Constants.StatusCodes.ACTIVE);
        response = api.createIngredient(ingredient);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        // test-case: create new Ingredient 1 by POST by Customer
        api.login(CUSTOMER_USER);
        response = api.createIngredient(ingredient);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void updateIngredientWithoutAuthorization() throws Exception {

        ResponseEntity<Ingredient> response;

        api.login(XIPLI_ADMIN);

        Ingredient ingredient = new Ingredient("wheat",Constants.StatusCodes.ACTIVE);
        response = api.createIngredient(ingredient);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Ingredient createdIngredient = response.getBody();
        assertNotNull(createdIngredient);
        assertTrue(createdIngredient.getIngredientId() > 0);
        ingredient.setIngredientId(createdIngredient.getIngredientId());
        assertEquals(ingredient, createdIngredient);

        api.logout();
        response = api.updateIngredient(createdIngredient.getIngredientId(),createdIngredient);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        api.login(CUSTOMER_USER);
        response = api.updateIngredient(createdIngredient.getIngredientId(),createdIngredient);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        api.login(XIPLI_ADMIN);
        response = api.updateIngredient(createdIngredient.getIngredientId(),createdIngredient);
        assertEquals(HttpStatus.OK, response.getStatusCode());
   }

   @Test
    public void deleteShopWithoutAuthorization() throws Exception {

        ResponseEntity<Ingredient> response;

        api.login(XIPLI_ADMIN);

       Ingredient ingredient = new Ingredient("wheat",Constants.StatusCodes.ACTIVE);
       response = api.createIngredient(ingredient);
       assertEquals(HttpStatus.CREATED, response.getStatusCode());
       Ingredient createdIngredient = response.getBody();
       assertNotNull(createdIngredient);
       assertTrue(createdIngredient.getIngredientId() > 0);
       ingredient.setIngredientId(createdIngredient.getIngredientId());
       assertEquals(ingredient, createdIngredient);

        api.logout();
        response = api.deleteIngredient(createdIngredient.getIngredientId());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        api.login(CUSTOMER_USER);
        response = api.deleteIngredient(createdIngredient.getIngredientId());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        api.login(XIPLI_ADMIN);
        response = api.deleteIngredient(createdIngredient.getIngredientId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void createAndUpdateAndDeleteShop() throws Exception {

        api.login(XIPLI_ADMIN);

        ResponseEntity<Ingredient> response;

        // test-case: create new Ingredient by POST
        Ingredient ingredient = new Ingredient("wheat",Constants.StatusCodes.ACTIVE);
        response = api.createIngredient(ingredient);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Ingredient createdIngredient = response.getBody();
        assertNotNull(createdIngredient);
        assertTrue(createdIngredient.getIngredientId() > 0);
        ingredient.setIngredientId(createdIngredient.getIngredientId());
        assertEquals(ingredient, createdIngredient);


        // test-case: update Ingredient by PUT
        createdIngredient.setName("Wheat with colour");
        response = api.updateIngredient(createdIngredient.getIngredientId(), createdIngredient);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Ingredient updatedingredient = response.getBody();
        assertNotNull(updatedingredient);
        assertEquals(createdIngredient, updatedingredient);
        assertThat(updatedingredient.getName(), is(equalTo("Wheat with colour")));

        // test-case: delete Ingredient by DELETE
        response = api.deleteIngredient(createdIngredient.getIngredientId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // test-case: verify Ingredient is not returned new shop by POST
        response = api.getIngredient(createdIngredient.getIngredientId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void notFoundShopId() throws Exception {

        api.login(XIPLI_ADMIN);

        ResponseEntity<Ingredient> response;

        // test-case: GET
        response = api.getIngredient(Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // test-case: PUT
        response = api.updateIngredient(Integer.MAX_VALUE, new Ingredient());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // test-case: DELETE
        response = api.deleteIngredient(Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
