package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(Constants.V1)
public class StatusController {

    @RequestMapping(path = Constants.STATUS_ENDPOINT, method = RequestMethod.GET)
    public ResponseEntity getStatus() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "running");
        return ResponseEntity.ok(response);
    }

}
