package com.xiaoslab.coffee.api.apis;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StatusAPITest extends _BaseAPITest {

    @Test
    public void statusAsJsonString() throws Exception {
        ResponseEntity<String> entity = api.getStatusJson();
        String response = entity.getBody();
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals("{\"status\":\"running\"}", response);
    }

    @Test
    public void statusAsHashMap() throws Exception {
        ResponseEntity<Map> entity = api.getStatusMap();
        Map response = entity.getBody();
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertTrue(response.containsKey("status"));
        assertEquals(response.get("status"), "running");
    }

}