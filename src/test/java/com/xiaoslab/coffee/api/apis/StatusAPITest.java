package com.xiaoslab.coffee.api.apis;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StatusAPITest extends _BaseAPITest {

    static final String V1_STATUS_ENDPOINT = "/v1/status";

    @Test
    public void statusAsJsonString() throws Exception {
        ResponseEntity<String> entity = GET(V1_STATUS_ENDPOINT, String.class);
        String response = entity.getBody();
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals("{\"status\":\"running\"}", response);
    }

    @Test
    public void statusAsHashMap() throws Exception {
        ResponseEntity<Map> entity = GET(V1_STATUS_ENDPOINT, Map.class);
        Map response = entity.getBody();
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertTrue(response.containsKey("status"));
        assertEquals(response.get("status"), "running");
    }

}