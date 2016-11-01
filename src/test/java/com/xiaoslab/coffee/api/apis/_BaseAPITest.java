package com.xiaoslab.coffee.api.apis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private int port;

    @Value("http://localhost")
    private String host;

    @Autowired
    private TestRestTemplate template;

    protected Logger getLogger() {
        return Logger.getLogger(this.getClass());
    }

    protected String getBaseApiUrl() {
        return host + ":" + port;
    }

    protected <T> ResponseEntity<T> GET(String path, Class<T> objectType) {
        getLogger().info("Base Path: " + getBaseApiUrl());
        getLogger().info("Request: GET " + path);
//        ResponseEntity<T> entity = template.getForEntity(getBaseApiUrl() + path, objectType);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<T> entity = template.exchange(path, HttpMethod.GET, requestEntity, objectType);
        getLogger().info("Response: " + entity);
        return entity;
    }

    protected <T> ResponseEntity<List<T>> LIST(String path, Class<T> objectType) {
        ResponseEntity<List> entity = GET(path, List.class);
        List<T> body = parseListFromResponseEntity(entity, objectType);
        return new ResponseEntity<>(body, entity.getHeaders(), entity.getStatusCode());
    }

    protected <T> ResponseEntity<T> POST(String path, T body, Class<T> objectType) {
        getLogger().info("Base Path: " + getBaseApiUrl());
        getLogger().info("Request: POST " + path);
        getLogger().info("Request Body: " + body);
//        ResponseEntity<T> entity = template.postForEntity(getBaseApiUrl() + path, body, objectType);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        headers.set(HttpHeaders.AUTHORIZATION, "foo");
        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<T> entity = template.exchange(path, HttpMethod.POST, requestEntity, objectType);
        getLogger().info("Response: " + entity);
        return entity;
    }

    protected <T> ResponseEntity<T> PUT(String path, T body, Class<T> objectType) {
        throw new NotImplementedException("TODO"); //TODO
    }

    protected <T> ResponseEntity<T> DELETE(String path, Class<T> objectType) {
        throw new NotImplementedException("TODO"); //TODO
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
}