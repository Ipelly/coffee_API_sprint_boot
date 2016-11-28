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
    private static final String V1_ADDON_ROOT_PATH = Constants.V1 + Constants.SHOP_ENDPOINT + "/%s" + Constants.ADDON_ENDPOINT + "/";
    private static final String V1_ITEM_OPTION_ROOT_PATH = Constants.V1 + Constants.SHOP_ENDPOINT + "/%s" + Constants.ITEM_ENDPOINT + "/%s" + Constants.ITEMOPTION_ENDPOINT + "/";
    private static final String V1_USER_ROOT_PATH = Constants.V1 + Constants.USER_ENDPOINT + "/";


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
        ResponseEntity<List> entity = GET(path, List.class);
        List<T> body = parseListFromResponseEntity(entity, responseType);
        return new ResponseEntity<>(body, entity.getHeaders(), entity.getStatusCode());
    }

    public <T> ResponseEntity<T> POST(String path, Object requestBody, Class<T> responseType) {
        return exchange(path, HttpMethod.POST, requestBody, responseType);
    }

    public <T> ResponseEntity<T> PUT(String path, Object requestBody, Class<T> responseType) {
        return exchange(path, HttpMethod.PUT, requestBody, responseType);
    }

    public <T> ResponseEntity<T> DELETE(String path, Class<T> objectType) {
        return exchange(path, HttpMethod.DELETE, null, objectType);
    }

    private <T> ResponseEntity<T> exchange(String path, HttpMethod method, Object requestBody, Class<T> objectType) {
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
                return new ResponseEntity<>(convertMapToObject((Map)responseEntity.getBody(), objectType), responseEntity.getHeaders(), responseEntity.getStatusCode());
            } else if (responseEntity.getBody() instanceof List) {
                return new ResponseEntity(responseEntity.getBody(), responseEntity.getHeaders(), responseEntity.getStatusCode());
            }
        }
        return new ResponseEntity<>(null, responseEntity.getHeaders(), responseEntity.getStatusCode());
    }

    private static <T> List<T> parseListFromResponseEntity(ResponseEntity entity, Class<T> objectType) {
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

    private void setAuthorizationHeader(String authorization) {
        customHeaders.set(HttpHeaders.AUTHORIZATION, authorization);
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
        customHeaders.set(HttpHeaders.AUTHORIZATION, "Basic " + new String(Base64.getEncoder().encode((clientId + ":" + clientSecret).getBytes())));
        customHeaders.set(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
        tokenRequest.put("grant_type", Arrays.asList("password"));
        tokenRequest.put("username", Arrays.asList(username));
        tokenRequest.put("password", Arrays.asList(password));
        ResponseEntity<TokenResponse> responseEntity = POST(Constants.TOKEN_ENDPOINT, tokenRequest, TokenResponse.class);
        customHeaders.set(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        String access_token = responseEntity.getBody().getAccess_token();
        Assert.assertNotNull("access_token is null", access_token);
        setAuthorizationHeader("Bearer " + access_token);
        return responseEntity;
    }

    private static class TokenResponse {

        private String access_token;
        private String refresh_token;
        private String token_type;
        private Long expires_in;
        private String scope;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public Long getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(Long expires_in) {
            this.expires_in = expires_in;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
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

    public ResponseEntity<User> getUser(long shopId) {
        return GET(V1_USER_ROOT_PATH + shopId, User.class);
    }

    public ResponseEntity<List<User>> listUsers() {
        return LIST(V1_USER_ROOT_PATH, User.class);
    }

    public ResponseEntity<List<User>> listUsers(String queryParams) {
        return LIST(V1_USER_ROOT_PATH + queryParams, User.class);
    }

    public ResponseEntity<User> createUser(User shop) {
        return POST(V1_USER_ROOT_PATH, shop, User.class);
    }

    public ResponseEntity<User> updateUser(long shopId, User shop) {
        return PUT(V1_USER_ROOT_PATH + shopId, shop, User.class);
    }

    public ResponseEntity<User> deleteUser(long shopId) {
        return DELETE(V1_USER_ROOT_PATH + shopId, User.class);
    }

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


    public ResponseEntity<Item> getItem(Item item, long itemId){return GET(String.format(V1_ITEM_ROOT_PATH, item.getShopId()) + itemId, Item.class);}

    public ResponseEntity<List<Item>> listItem(long shopId) {
        return LIST(String.format(V1_ITEM_ROOT_PATH, shopId), Item.class);
    }

    public ResponseEntity<List<Item>> listItem(long shopId, String queryParams) {
        return LIST(String.format(V1_ITEM_ROOT_PATH, shopId) + queryParams, Item.class);
    }

    public ResponseEntity<Item> createItem(Item item) {
        return POST(String.format(V1_ITEM_ROOT_PATH, item.getShopId()), item, Item.class);
    }

    public ResponseEntity<Item> updateItem(Item item, long itemId) {
        return PUT(String.format(V1_ITEM_ROOT_PATH, item.getShopId()) + itemId, item, Item.class);
    }

    public ResponseEntity<Item> deleteItem(Item item, long itemId) {
        return DELETE(String.format(V1_ITEM_ROOT_PATH, item.getShopId()) + itemId, Item.class);
    }


    public ResponseEntity<ItemOption> createItemOption(ItemOption itemOption,long shopId) {
        return POST(String.format (V1_ITEM_OPTION_ROOT_PATH, shopId, itemOption.getItemId()), itemOption, ItemOption.class );
    }

    public ResponseEntity<ItemOption> updateItemOption(ItemOption itemOption,long shopId,long itemoptionid) {
        return PUT(String.format(V1_ITEM_OPTION_ROOT_PATH,shopId, itemOption.getItemId ()) + itemoptionid, itemOption, ItemOption.class);
    }

    public ResponseEntity<ItemOption> deleteItemOption(ItemOption itemOption,long shopId,long itemoptionid) {
        return DELETE(String.format(V1_ITEM_OPTION_ROOT_PATH,shopId, itemOption.getItemId ()) + itemoptionid, ItemOption.class);
    }

    public ResponseEntity<ItemOption> getItemOption(long shopId, long itemId, long itemOptionId) {
        return GET(String.format (V1_ITEM_OPTION_ROOT_PATH, shopId,itemId) + itemOptionId, ItemOption.class);
    }

    public ResponseEntity<List<ItemOption>> listItemOption(long shopId, long itemId) {
        //String path = V1_SHOP_ROOT_PATH + "/"+ String.valueOf(shopId) + "/" + Constants.ITEM_ENDPOINT + "/" + String.valueOf(itemId) + "/" + Constants.ITEMOPTION_ENDPOINT + "/";
        return LIST(String.format (V1_ITEM_OPTION_ROOT_PATH, shopId,itemId), ItemOption.class);
    }


    // Addon

    public ResponseEntity<Addon> getAddon(long shopid, long addonId){
        return GET(String.format(V1_ADDON_ROOT_PATH, shopid) + addonId, Addon.class);
    }

    public ResponseEntity<List<Addon>> listOfAddon(long shopId) {
        return LIST(String.format(V1_ADDON_ROOT_PATH, shopId), Addon.class);
    }

    public ResponseEntity<List<Addon>> listOfAddon(long shopId, String queryParams) {
        return LIST(String.format(V1_ADDON_ROOT_PATH, shopId) + queryParams, Addon.class);
    }

    public ResponseEntity<Addon> createAddon(Addon addon) {
        return POST(String.format(V1_ADDON_ROOT_PATH, addon.getShopId ()), addon, Addon.class);
    }

    public ResponseEntity<Addon> updateAddon(Addon addon, long addonId) {
        return PUT(String.format(V1_ADDON_ROOT_PATH, addon.getShopId ()) + addonId, addon, Addon.class);
    }

    public ResponseEntity<Addon> deleteAddon(Addon addon, long addonId) {
        return DELETE(String.format(V1_ADDON_ROOT_PATH, addon.getShopId ()) + addonId, Addon.class);
    }
}
