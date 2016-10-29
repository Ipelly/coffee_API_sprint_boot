package com.xiaoslab.coffee.api.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1") //FIXME
public class UserController {

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public Object getProfile() {
        //FIXME: add authorization
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
