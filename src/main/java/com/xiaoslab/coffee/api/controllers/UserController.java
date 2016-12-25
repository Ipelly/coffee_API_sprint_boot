package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.PasswordUpdateRequest;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.objects.User.NewUserRequest;
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

import javax.ws.rs.NotAllowedException;
import java.net.URI;


@RestController
@RequestMapping(path = Constants.V1 + Constants.USER_ENDPOINT)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetService passwordResetService;


    @RequestMapping(path = "/self", method = RequestMethod.GET)
    public ResponseEntity getSelf() {
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody NewUserRequest newUserRequest) {
        User newUser = User.copyFromNewUserRequest(newUserRequest);
        User createdUser = userService.registerNewUser(newUser);
        URI location = AppUtility.buildCreatedLocation(AppUtility.getCurrentRequest().getServletPath().replace("/register", ""), createdUser.getUserId());
        return ResponseEntity.created(location).body(createdUser);
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody User user) {
        throw new NotAllowedException("Not Allowed");
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
