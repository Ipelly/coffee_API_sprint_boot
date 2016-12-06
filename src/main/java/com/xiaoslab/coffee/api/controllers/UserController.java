package com.xiaoslab.coffee.api.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xiaoslab.coffee.api.objects.PasswordUpdateRequest;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.services.PasswordResetService;
import com.xiaoslab.coffee.api.services.UserService;
import com.xiaoslab.coffee.api.utility.AppUtility;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@RestController
@RequestMapping(path = Constants.V1 + Constants.USER_ENDPOINT)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetService passwordResetService;

    public static class NewUserRequest extends User {

        @Override
        @JsonProperty
        public String getPassword() {
            return this.password;
        }
    }

    @RequestMapping(path = "/self", method = RequestMethod.GET)
    public ResponseEntity getSelf() {
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody NewUserRequest newUserRequest) {
        User newUser = new User();
        newUser.setPassword(newUserRequest.getPassword());
        newUser.setEmailAddress(newUserRequest.getEmailAddress());
        newUser.setFirstName(newUserRequest.getFirstName());
        newUser.setLastName(newUserRequest.getLastName());
        newUser.setName(newUserRequest.getName());
        User createdUser = userService.registerNewUser(newUser);
        URI location = AppUtility.buildCreatedLocation(AppUtility.getCurrentRequest().getServletPath().replace("/register", ""), createdUser.getUserId());
        return ResponseEntity.created(location).body(createdUser);
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody User user) {
        User createdUser = userService.create(user);
        URI location = AppUtility.buildCreatedLocation(createdUser.getUserId());
        return ResponseEntity.created(location).body(createdUser);
    }

    @RequestMapping(path = "/password/reset", method = RequestMethod.POST)
    public ResponseEntity passwordReset(@RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        User user = userService.getUserByEmailAddress(passwordUpdateRequest.getEmailAddress());
        if (user == null) {
            throw new IllegalArgumentException("Invalid user");
        }
        passwordResetService.createAndEmailCodeForUser(user.getUserId());
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "/password/update", method = RequestMethod.POST)
    public ResponseEntity passwordUpdate(@RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        User user = userService.getUserByEmailAddress(passwordUpdateRequest.getEmailAddress());
        if (user == null) {
            throw new IllegalArgumentException("Invalid user");
        }
        passwordResetService.updatePasswordForUser(passwordUpdateRequest);
        return ResponseEntity.ok().build();
    }

}
