package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.Addon;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.objects.User;
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
public class AddonAPITest extends _BaseAPITest {

    long shopIdForTest,itemIdFortest;

    @Test
    public void createAndGetAndListOfAddon() throws Exception {

        ResponseEntity<List<Addon>> addonListResponse;
        ResponseEntity<Addon> addonResponse;

        preRequisiteTestScenarioForAddon();


        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        // test-case: create new addon for the shop by POST
        Addon createAddon = addonCreateWithAssertion();

        // test-case: list of Addon by GET
        api.login(CUSTOMER_USER);

        addonListResponse = api.listOfAddon(shopIdForTest);
        assertEquals(HttpStatus.OK, addonListResponse.getStatusCode());
        List<Addon> addonList = addonListResponse.getBody();
        assertEquals(1, addonList.size());
        //assertEquals(createAddon, addonList.get(0));

    }

    @Test
    public void createAddonWithoutAuthorization() throws Exception {

        preRequisiteTestScenarioForAddon();

        ResponseEntity<Addon> response;

        Addon addon = testUtils.setupAddonObject (shopIdForTest);

        response = api.createAddon(addon);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        api.login(CUSTOMER_USER);
        response = api.createAddon(addon);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    }

    @Test
    public void updateAddonWithoutAuthorization() throws Exception {

        ResponseEntity<Addon> response;
        preRequisiteTestScenarioForAddon();


        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        // test-case: create new item option for the item1 by POST
        Addon createAddon = addonCreateWithAssertion();

        api.logout();


        createAddon.setName("Extra Large X");
        response = api.updateAddon(createAddon,createAddon.getAddon_id());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        api.login(CUSTOMER_USER);
        response = api.updateAddon(createAddon,createAddon.getAddon_id() );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        api.login(XIPLI_ADMIN);
        response = api.updateAddon(createAddon,createAddon.getAddon_id() );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    }


    @Test
    public void deleteAddonWithoutAuthorization() throws Exception {

        ResponseEntity<Addon> response;
        preRequisiteTestScenarioForAddon();


        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        // test-case: create new addon for the shop by POST
        Addon createdAddon = addonCreateWithAssertion();

        api.logout();


        //createItemOption1.setName("Samll in Size");
        response = api.deleteAddon(createdAddon,createdAddon.getAddon_id ());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        api.login(CUSTOMER_USER);
        response = api.deleteAddon(createdAddon,createdAddon.getAddon_id ());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        api.login(XIPLI_ADMIN);
        response = api.deleteAddon(createdAddon,createdAddon.getAddon_id ());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    }

    @Test
    public void createAndUpdateAndDeleteAddonId() throws Exception {


        ResponseEntity<Addon> response;
        preRequisiteTestScenarioForAddon();


        // Auth : Login as SHOP_ADMIN
        User shopAdmin = testUtils.createShopAdminUser(shopIdForTest);
        api.login(shopAdmin);

        // test-case: create new addon for the shop by POST
        Addon createdAddon = addonCreateWithAssertion();

        // test-case: update item option by PUT
        createdAddon.setName("Extra Large X");
        response = api.updateAddon(createdAddon,createdAddon.getAddon_id());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Addon updatedAddon = response.getBody();
        assertNotNull(updatedAddon);
        //assertEquals(createdAddon, updatedAddon);
        assertThat(updatedAddon.getName(), is(equalTo("Extra Large X")));

        // test-case: Delete item option by PUT
        response = api.deleteAddon(createdAddon,createdAddon.getAddon_id ());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // test-case: verify shop is not returned new shop by POST
        response = api.getAddon(shopIdForTest,createdAddon.getAddon_id ());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void notFoundAddonId() throws Exception {

        api.login(XIPLI_ADMIN);

        ResponseEntity<Addon> response;

        // test-case: GET

        response = api.getAddon(shopIdForTest,Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // test-case: PUT
        response = api.updateAddon(new Addon ( ),Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // test-case: DELETE
        response = api.deleteAddon ( new Addon(), Integer.MAX_VALUE);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private void preRequisiteTestScenarioForAddon(){

        api.login(XIPLI_ADMIN);

        ResponseEntity<List<Shop>> listResponse;
        ResponseEntity<Shop> response;

        // test-case: create new shop by POST
        Shop shop1 = testUtils.setupShopObject();
        response = api.createShop(shop1);
        Shop createdShop1 = response.getBody();
        shopIdForTest = createdShop1.getShopId();

        api.logout ();

    }
    private Addon addonCreateWithAssertion(){
        ResponseEntity<List<Addon>> addonListResponse;
        ResponseEntity<Addon> addonResponse;
        Addon addon = testUtils.setupAddonObject(shopIdForTest);

        addonResponse = api.createAddon(addon);
        assertEquals(HttpStatus.CREATED, addonResponse.getStatusCode());
        Addon createdAddon = addonResponse.getBody();
        assertNotNull(createdAddon);
        assertTrue(createdAddon.getAddon_id () > 0);
        addon.setAddon_id(createdAddon.getAddon_id());
        //assertEquals(addon, createdAddon);
        return createdAddon;
    }
}
