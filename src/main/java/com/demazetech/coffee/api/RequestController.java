package com.demazetech.coffee.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class RequestController {

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public Object status() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "running");
        return response;
    }
}
