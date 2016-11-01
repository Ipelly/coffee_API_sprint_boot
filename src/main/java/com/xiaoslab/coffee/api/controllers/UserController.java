package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.services.UserService;
import com.xiaoslab.coffee.api.utility.AppUtility;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@RestController
@RequestMapping(path = Constants.V1 + Constants.USER_ENDPOINT)
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/self", method = RequestMethod.GET)
    public ResponseEntity getSelf() {
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity register(User newUser) {
        User createdUser = userService.registerNewUser(newUser);
        URI location = AppUtility.buildCreatedLocation(AppUtility.getCurrentRequest().getServletPath().replace("/register", ""), createdUser.getUserId());
        return ResponseEntity.created(location).body(createdUser);
    }

}
