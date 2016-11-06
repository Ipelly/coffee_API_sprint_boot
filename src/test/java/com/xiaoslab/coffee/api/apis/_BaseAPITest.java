package com.xiaoslab.coffee.api.apis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.utilities.APITestUtils;
import com.xiaoslab.coffee.api.utilities.TestConstants;
import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

//Rollback does not work for integration tests, because spring runs the 
//web application on a different thread than the test. As an alternative,
//we are removing test data by using @Sql annotation with a cleanup script.
//@Transactional
//@Rollback(true)
@Sql("classpath:database/ClearDatabaseForTest.sql")
public abstract class _BaseAPITest {

    @Value("${local.server.port}")
    protected int port;

    @Value("http://localhost")
    protected String host;

    @Autowired
    protected TestRestTemplate template;

    @Autowired
    protected APITestUtils apiTestUtils;

    protected HttpHeaders customHeaders = new HttpHeaders();

    protected User CUSTOMER_USER;
    protected User XIPLI_ADMIN;

    @Before
    public void setupUsers() {
        if (CUSTOMER_USER == null) CUSTOMER_USER = apiTestUtils.createCustomerUser();
        if (XIPLI_ADMIN == null) XIPLI_ADMIN = apiTestUtils.createXipliAdminUser();
    }

    protected Logger getLogger() {
        return Logger.getLogger(this.getClass());
    }

    protected String getBaseApiUrl() {
        return host + ":" + port;
    }

    protected <T> ResponseEntity<T> GET(String path, Class<T> responseType) {
        return exchange(path, HttpMethod.GET, null, responseType);
    }

    protected <T> ResponseEntity<List<T>> LIST(String path, Class<T> responseType) {
        ResponseEntity<List> entity = GET(path, List.class);
        List<T> body = parseListFromResponseEntity(entity, responseType);
        return new ResponseEntity<>(body, entity.getHeaders(), entity.getStatusCode());
    }

    protected <T> ResponseEntity<T> POST(String path, Object requestBody, Class<T> responseType) {
        return exchange(path, HttpMethod.POST, requestBody, responseType);
    }

    protected <T> ResponseEntity<T> PUT(String path, Object requestBody, Class<T> responseType) {
        return exchange(path, HttpMethod.PUT, requestBody, responseType);
    }

    protected <T> ResponseEntity<T> DELETE(String path, Class<T> objectType) {
        return exchange(path, HttpMethod.DELETE, null, objectType);
    }

    private <T> ResponseEntity<T> exchange(String path, HttpMethod method, Object requestBody, Class<T> objectType) {
        getLogger().info("Request: " + method.name() + " " + getBaseApiUrl() + path);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        headers.setAll (customHeaders.toSingleValueMap());

        if (requestBody != null) getLogger().info("Request Body: " + requestBody);
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<T> responseEntity = template.exchange(path, method, requestEntity, objectType);

        getLogger().info("Response Code: " + responseEntity.getStatusCode());
        getLogger().info("Response Body: " + responseEntity.getBody());

        return responseEntity;
    }

    private static <T> List<T> parseListFromResponseEntity(ResponseEntity entity, Class<T> objectType) {
        List<Map> listOfMap = (List) entity.getBody();
        List<T> listOfObjects = new ArrayList<>();
        listOfMap.forEach(item -> listOfObjects.add((T) convertMapToObject(item, objectType)));
        return listOfObjects;
    }

    private static <T> T convertMapToObject(Map map, Class<T> objectType) {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(map, objectType);
    }

    protected void setAuthorizationHeader(String authorization) {
        customHeaders.set(HttpHeaders.AUTHORIZATION, authorization);
    }

    protected void logoutFromOAuth2() {
        customHeaders.remove(HttpHeaders.AUTHORIZATION);
    }

    protected ResponseEntity loginWithOAuth2(User user) {
        return loginWithOAuth2(user.getEmailAddress());
    }

    protected ResponseEntity loginWithOAuth2(String username) {
        return loginWithOAuth2(username, TestConstants.TEST_DEFAULT_PASSWORD);
    }

    protected ResponseEntity loginWithOAuth2(String username, String password) {
        return loginWithOAuth2(TestConstants.TEST_OAUTH_CLIENT_ID, TestConstants.TEST_OAUTH_CLIENT_SECRET, username, password);
    }

    protected ResponseEntity loginWithOAuth2(String clientId, String clientSecret, String username, String password) {
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

    protected static class TokenResponse {

        private String access_token;
        private String refresh_token;
        private String token_type;
        private Long expires_in;

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

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
        }
    }

}