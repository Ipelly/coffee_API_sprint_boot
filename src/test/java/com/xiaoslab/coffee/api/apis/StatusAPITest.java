package com.xiaoslab.coffee.api.apis;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StatusAPITest extends _BaseAPITest {

    static final String STATUS_ENDPOINT = "/status";

    @Test
    public void statusAsJsonString() throws Exception {
        ResponseEntity entity = get(STATUS_ENDPOINT, String.class);
        String response = (String) entity.getBody();
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals("{\"status\":\"running\"}", response);
    }

    @Test
    public void statusAsHashMap() throws Exception {
        ResponseEntity entity = get(STATUS_ENDPOINT, HashMap.class);
        HashMap response = (HashMap) entity.getBody();
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertTrue(response.containsKey("status"));
        assertEquals(response.get("status"), "running");
    }

}