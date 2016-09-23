package com.demazetech.coffee.api;

import com.demazetech.coffee.api.services.FacebookService;
import com.demazetech.coffee.api.services.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class RequestController {

    @Autowired
    private FacebookService facebookService;

    @Autowired
    MenuItemService menuItemService;

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public Object getStatus() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "running");
        return response;
    }

    @RequestMapping(value = "/test-menu", method = RequestMethod.GET)
    public Object getTestMenu() {
        return menuItemService.listMenuItems();
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public Object getProfile() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
