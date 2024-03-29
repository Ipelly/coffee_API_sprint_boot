package com.xiaoslab.coffee.api.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoslab.coffee.api.objects.*;
import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class APIAdapter {


    // API Endpoints. Add new ones here as static final String
    private static final String V1_STATUS_ENDPOINT = Constants.V1 + Constants.STATUS_ENDPOINT;
    private static final String V1_SHOP_ROOT_PATH = Constants.V1 + Constants.SHOP_ENDPOINT + "/";
    private static final String V1_ITEM_ROOT_PATH = Constants.V1 + Constants.SHOP_ENDPOINT + "/%s" + Constants.ITEM_ENDPOINT + "/";
    private static final String V1_ITEM_OPTION_ROOT_PATH = Constants.V1 + Constants.SHOP_ENDPOINT + "/%s" + Constants.ITEM_ENDPOINT + "/%s" + Constants.OPTION_ENDPOINT + "/";
    private static final String V1_ITEM_ADDON_ROOT_PATH = Constants.V1 + Constants.SHOP_ENDPOINT + "/%s" + Constants.ITEM_ENDPOINT + "/%s" + Constants.ADDON_ENDPOINT + "/";
    private static final String V1_USER_ROOT_PATH = Constants.V1 + Constants.USER_ENDPOINT + "/";
    private static final String V1_SHOP_USER_ROOT_PATH = Constants.V1 + Constants.SHOP_ENDPOINT + "/%s" + Constants.USER_ENDPOINT + "/";
    private static final String V1_INGREDIENT_ROOT_PATH = Constants.V1 + Constants.INGREDIENT_ENDPOINT + "/";
    private static final String V1_CATEGORY_ROOT_PATH = Constants.V1 + Constants.SHOP_ENDPOINT + "/%s" + Constants.CATEGORY_ENDPOINT + "/";

    // REST template values
    private int port;
    private String host;
    private TestRestTemplate template;
    private HttpHeaders customHeaders = new HttpHeaders();
    private static RequestEntity<Object> lastRequest;
    private static ResponseEntity<Object> lastResponse;
    private static final ObjectMapper jsonMapper = new ObjectMapper();


    // Default constructor
    public APIAdapter() {}


    // Optional constructor
    public APIAdapter(TestRestTemplate template, String host, int port) {
        this.template = template;
        this.host = host;
        this.port = port;
    }

    // Logger for this class
    private static final Logger LOGGER = Logger.getLogger(APIAdapter.class);

    public static RequestEntity getLastRequest() {
        return lastRequest;
    }

    public static ResponseEntity getLastResponse() {
        return lastResponse;
    }

    public String getLastResponseAsString() {
        return lastResponse.toString();
    }

    private static void clearLastRequestAndResponse() {
        lastRequest = null;
        lastResponse = null;
    }

    public String getBaseApiUrl() {
        return host + ":" + port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setTemplate(TestRestTemplate template) {
        this.template = template;
    }

    public <T> ResponseEntity<T> GET(String path, Class<T> responseType) {
        return exchange(path, HttpMethod.GET, null, responseType);
    }

    public <T> ResponseEntity<List<T>> LIST(String path, Class<T> responseType) {
        ResponseEntity<List> responseEntity = GET(path, List.class);
        List<T> list = parseListFromResponseEntity(responseEntity, responseType);
        return new ResponseEntity<>(list, responseEntity.getHeaders(), responseEntity.getStatusCode());
    }

    public <T> ResponseEntity<T> POST(String path, Object requestBody, Class<T> responseType) {
        return exchange(path, HttpMethod.POST, requestBody, responseType);
    }

    public <T> ResponseEntity<T> PUT(String path, Object requestBody, Class<T> responseType) {
        return exchange(path, HttpMethod.PUT, requestBody, responseType);
    }

    public <T> ResponseEntity<List<T>> PUT_ARRAY(String path, Object requestBody, Class<T> responseType) {
        ResponseEntity<List> responseEntity = PUT(path, requestBody, List.class);
        List<T> list = parseListFromResponseEntity(responseEntity, responseType);
        return new ResponseEntity<>(list, responseEntity.getHeaders(), responseEntity.getStatusCode());
    }

    public <T> ResponseEntity<T> DELETE(String path, Class<T> objectType) {
        return exchange(path, HttpMethod.DELETE, null, objectType);
    }

    private <T> ResponseEntity<T> exchange(String path, HttpMethod method, Object requestBody, Class<T> responseType) {
        LOGGER.info("");
        LOGGER.info("Request: " + method.name() + " " + getBaseApiUrl() + path);

        clearLastRequestAndResponse();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        headers.setAll (customHeaders.toSingleValueMap());

        if (requestBody != null) LOGGER.info("Request Body: " + requestBody);

        URI uri = null;
        try {
            String[] splitPath = path.split("\\?", 2);
            String fullPath = getBaseApiUrl() + splitPath[0];
            if (splitPath.length > 1) {
                fullPath += new URLCodec().encode(splitPath[1]);
            }
            uri = new URI(fullPath);
        } catch (URISyntaxException|EncoderException syntaxErr) {
            Assert.fail("<" + getBaseApiUrl() + path + "> is not a valid URI");
        }

        RequestEntity<Object> requestEntity = new RequestEntity<>(requestBody, headers, method, uri);
        lastRequest = requestEntity;

        ResponseEntity<Object> responseEntity = template.exchange(path, method, requestEntity, Object.class);
        lastResponse = new ResponseEntity<>(convertObjectToJson(responseEntity.getBody()), responseEntity.getHeaders(), responseEntity.getStatusCode());

        LOGGER.info("Response Code: " + lastResponse.getStatusCode());
        LOGGER.info("Response Body: " + lastResponse.getBody());

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            if (responseEntity.getBody() instanceof Map) {
                return new ResponseEntity<>(convertMapToObject((Map)responseEntity.getBody(), responseType), responseEntity.getHeaders(), responseEntity.getStatusCode());
            } else if (responseEntity.getBody() instanceof List) {
                return new ResponseEntity(responseEntity.getBody(), responseEntity.getHeaders(), responseEntity.getStatusCode());
            }
        }
        return new ResponseEntity<>(null, responseEntity.getHeaders(), responseEntity.getStatusCode());
    }

    private static <T> List<T> parseListFromResponseEntity(ResponseEntity entity, Class<T> objectType) {
        if (entity == null || entity.getBody() == null) return null;
        List<Map> listOfMap = (List) entity.getBody();
        List<T> listOfObjects = new ArrayList<>();
        listOfMap.forEach(item -> listOfObjects.add((T) convertMapToObject(item, objectType)));
        return listOfObjects;
    }

    private static <T> T convertMapToObject(Map map, Class<T> objectType) {
        if (objectType.equals(String.class)) {
            return (T) convertObjectToJson(map);
        }
        return jsonMapper.convertValue(map, objectType);
    }

    public static String convertObjectToJson(Object object) {
        try {
            return jsonMapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            Assert.fail(ex.getMessage());
            return null;
        }
    }

    public void logout() {
        customHeaders.remove(HttpHeaders.AUTHORIZATION);
    }

    public ResponseEntity login(User user) {
        return login(user.getEmailAddress());
    }

    public ResponseEntity login(String username) {
        return login(username, TestConstants.TEST_DEFAULT_PASSWORD);
    }

    public ResponseEntity login(String username, String password) {
        return login(TestConstants.TEST_OAUTH_CLIENT_ID, TestConstants.TEST_OAUTH_CLIENT_SECRET, username, password);
    }

    public ResponseEntity login(String clientId, String clientSecret, String username, String password) {
        // set basic authorization headers bas64(clientId:clientSecret)
        customHeaders.set(HttpHeaders.AUTHORIZATION, "Basic " + new String(Base64.getEncoder().encode((clientId + ":" + clientSecret).getBytes())));
        customHeaders.set(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
        // set url encoded form parameters for password grant
        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
        tokenRequest.put("grant_type", Arrays.asList("password"));
        tokenRequest.put("username", Arrays.asList(username));
        tokenRequest.put("password", Arrays.asList(password));
        // make the oauth/token request
        ResponseEntity<TokenResponse> responseEntity = POST(Constants.TOKEN_ENDPOINT, tokenRequest, TokenResponse.class);
        // verify access token was generated successfully
        Assert.assertNotNull("response body for token is invalid", responseEntity.getBody());
        String accessToken = responseEntity.getBody().access_token;
        Assert.assertNotNull("access_token is null", accessToken);
        customHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        customHeaders.set(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return responseEntity;
    }

    private static class TokenResponse {

        public String access_token;
        public String refresh_token;
        public String token_type;
        public Long expires_in;
        public String scope;
        public String error;
        public String error_description;

        public void setError(String error) {
            this.error = error;
        }

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
        }
    }

    // ----------------------- Xipli API Endpoints Start -----------------------

    public ResponseEntity<String> getStatusJson() {
        return GET(V1_STATUS_ENDPOINT, String.class);
    }

    public ResponseEntity<Map> getStatusMap() {
        return GET(V1_STATUS_ENDPOINT, Map.class);
    }

    // Users -----------------------

    public ResponseEntity<User> getUser(long shopId) {
        return GET(V1_USER_ROOT_PATH + shopId, User.class);
    }

    public ResponseEntity<List<User>> listUsers() {
        return LIST(V1_USER_ROOT_PATH, User.class);
    }

    public ResponseEntity<List<User>> listUsers(String queryParams) {
        return LIST(V1_USER_ROOT_PATH + queryParams, User.class);
    }

    public ResponseEntity<User> createUser(User user) {
        return POST(V1_USER_ROOT_PATH, user, User.class);
    }

    public ResponseEntity<User> updateUser(long shopId, User shop) {
        return PUT(V1_USER_ROOT_PATH + shopId, shop, User.class);
    }

    public ResponseEntity<User> deleteUser(long userId) {
        return DELETE(V1_USER_ROOT_PATH + userId, User.class);
    }

    public ResponseEntity<User> registerUser(Object user) {
        return POST(V1_USER_ROOT_PATH + "register", user, User.class);
    }

    public ResponseEntity passwordReset(PasswordUpdateRequest passwordUpdateRequest) {
        return POST(V1_USER_ROOT_PATH + "password/reset", passwordUpdateRequest, PasswordUpdateRequest.class);
    }

    public ResponseEntity passwordUpdate(PasswordUpdateRequest passwordUpdateRequest) {
        return POST(V1_USER_ROOT_PATH + "password/update", passwordUpdateRequest, PasswordUpdateRequest.class);
    }

    // Shop User -----------------------

    public ResponseEntity<User> createShopUser(long shopId, User user) {
        return POST(String.format(V1_SHOP_USER_ROOT_PATH, shopId), user, User.class);
    }

    public ResponseEntity<List<User>> listShopUsers(long shopId) {
        return LIST(String.format(V1_SHOP_USER_ROOT_PATH, shopId), User.class);
    }

    public ResponseEntity<List<User>> listShopUsers(long shopId, String queryParams) {
        return LIST(String.format(V1_SHOP_USER_ROOT_PATH + queryParams, shopId), User.class);
    }

    // Shops -----------------------

    public ResponseEntity<Shop> getShop(long shopId) {
        return GET(V1_SHOP_ROOT_PATH + shopId, Shop.class);
    }

    public ResponseEntity<List<Shop>> listShop() {
        return LIST(V1_SHOP_ROOT_PATH, Shop.class);
    }

    public ResponseEntity<List<Shop>> listShop(String queryParams) {
        return LIST(V1_SHOP_ROOT_PATH + queryParams, Shop.class);
    }

    public ResponseEntity<Shop> createShop(Shop shop) {
        return POST(V1_SHOP_ROOT_PATH, shop, Shop.class);
    }

    public ResponseEntity<Shop> updateShop(long shopId, Shop shop) {
        return PUT(V1_SHOP_ROOT_PATH + shopId, shop, Shop.class);
    }

    public ResponseEntity<Shop> deleteShop(long shopId) {
        return DELETE(V1_SHOP_ROOT_PATH + shopId, Shop.class);
    }

    // Items -----------------------

    public ResponseEntity<Item> createItem(long shopId, Item item) {
        return POST(String.format(V1_ITEM_ROOT_PATH, shopId), item, Item.class);
    }

    public ResponseEntity<Item> updateItem(long shopId, long itemId, Item item) {
        return PUT(String.format(V1_ITEM_ROOT_PATH, shopId) + itemId, item, Item.class);
    }

    public ResponseEntity<Item> deleteItem(long shopId, long itemId) {
        return DELETE(String.format(V1_ITEM_ROOT_PATH, shopId) + itemId, Item.class);
    }

    public ResponseEntity<Item> getItem(long shopId, long itemId){
        return GET(String.format(V1_ITEM_ROOT_PATH, shopId) + itemId, Item.class);
    }

    public ResponseEntity<List<Item>> listItem(long shopId) {
        return LIST(String.format(V1_ITEM_ROOT_PATH, shopId) , Item.class);
    }

    public ResponseEntity<List<Item>> listItem(long shopId, String queryParams) {
        return LIST(String.format(V1_ITEM_ROOT_PATH, shopId) + queryParams, Item.class);
    }

    public ResponseEntity<List<Item>> listItemForCategory(long shopId, long categoryId) {
        return listItem(shopId, "?categoryId=" + categoryId);
    }

    public ResponseEntity<List<Item>> listItemForCategory(long shopId, long categoryId, long size) {
        return listItem(shopId, "?categoryId=" + categoryId + "&size=" + size);
    }

    // Category -----------------------

    public ResponseEntity<Category> createCategory(long shopId, Category category) {
        return POST(String.format (V1_CATEGORY_ROOT_PATH, shopId, category.getCategoryId()), category, Category.class );
    }

    public ResponseEntity<Category> updateCategory(long shopId, long categoryId, Category category) {
        return PUT(String.format(V1_CATEGORY_ROOT_PATH,shopId, category.getCategoryId ()) + categoryId, category, Category.class);
    }

    public ResponseEntity<Category> deleteCategory(long shopId, long categoryId) {
        return DELETE(String.format(V1_CATEGORY_ROOT_PATH, shopId) + categoryId, Category.class);
    }

    public ResponseEntity<Category> getCategory(long shopId, long categoryId) {
        return GET(String.format (V1_CATEGORY_ROOT_PATH, shopId) + categoryId, Category.class);
    }

    public ResponseEntity<List<Category>> listCategory(long shopId) {
        return LIST(String.format (V1_CATEGORY_ROOT_PATH, shopId), Category.class);
    }

    public ResponseEntity<List<Category>> listCategory(long shopId, String queryParams) {
        return LIST(String.format (V1_CATEGORY_ROOT_PATH + queryParams, shopId), Category.class);
    }

    // Item Options -----------------------

    public ResponseEntity<ItemOption> createItemOption(long shopId, long itemId, ItemOption itemOption) {
        return POST(String.format (V1_ITEM_OPTION_ROOT_PATH, shopId, itemId), itemOption, ItemOption.class );
    }

    public ResponseEntity<ItemOption> updateItemOption(long shopId, long itemId, long itemOptionId, ItemOption itemOption) {
        return PUT(String.format(V1_ITEM_OPTION_ROOT_PATH, shopId, itemId) + itemOptionId, itemOption, ItemOption.class);
    }

    public ResponseEntity<ItemOption> deleteItemOption(long shopId, long itemId, long itemOptionId) {
        return DELETE(String.format(V1_ITEM_OPTION_ROOT_PATH, shopId, itemId) + itemOptionId, ItemOption.class);
    }

    public ResponseEntity<ItemOption> getItemOption(long shopId, long itemId, long itemOptionId) {
        return GET(String.format (V1_ITEM_OPTION_ROOT_PATH, shopId, itemId) + itemOptionId, ItemOption.class);

    }

    public ResponseEntity<List<ItemOption>> updateAllItemOptions(long shopId, long itemId, List<ItemOption> itemOptions) {
        return PUT_ARRAY(String.format(V1_ITEM_OPTION_ROOT_PATH, shopId, itemId), itemOptions, ItemOption.class);
    }

    public ResponseEntity<List<ItemOption>> listItemOption(long shopId, long itemId) {
        return LIST(String.format (V1_ITEM_OPTION_ROOT_PATH, shopId, itemId), ItemOption.class);
    }

    // Item Addons -----------------------

    public ResponseEntity<ItemAddon> createItemAddon(long shopId, long itemId, ItemAddon itemAddon) {
        return POST(String.format (V1_ITEM_ADDON_ROOT_PATH, shopId, itemId), itemAddon, ItemAddon.class );
    }

    public ResponseEntity<ItemAddon> updateItemAddon(long shopId, long itemId, long itemAddonId, ItemAddon itemAddon) {
        return PUT(String.format(V1_ITEM_ADDON_ROOT_PATH, shopId, itemId) + itemAddonId, itemAddon, ItemAddon.class);
    }

    public ResponseEntity<ItemAddon> deleteItemAddon(long shopId, long itemId, long itemAddonId) {
        return DELETE(String.format(V1_ITEM_ADDON_ROOT_PATH, shopId, itemId) + itemAddonId, ItemAddon.class);
    }

    public ResponseEntity<ItemAddon> getItemAddon(long shopId, long itemId, long itemAddonId) {
        return GET(String.format (V1_ITEM_ADDON_ROOT_PATH, shopId, itemId) + itemAddonId, ItemAddon.class);

    }

    public ResponseEntity<List<ItemAddon>> updateAllItemAddons(long shopId, long itemId, List<ItemAddon> itemAddons) {
        return PUT_ARRAY(String.format(V1_ITEM_ADDON_ROOT_PATH, shopId, itemId), itemAddons, ItemAddon.class);
    }

    public ResponseEntity<List<ItemAddon>> listItemAddon(long shopId, long itemId) {
        return LIST(String.format (V1_ITEM_ADDON_ROOT_PATH, shopId, itemId), ItemAddon.class);
    }

}
