package com.demazetech.coffee.api.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity badRequest(HttpServletRequest request, Exception exception) {
        return ResponseEntity.badRequest().body(exception);
    }
}
