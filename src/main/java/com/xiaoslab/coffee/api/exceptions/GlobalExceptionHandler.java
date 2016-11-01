package com.xiaoslab.coffee.api.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler({IllegalArgumentException.class, DataIntegrityViolationException.class})
    public ResponseEntity badRequest(HttpServletRequest request, Exception exception) {
        return ResponseEntity.badRequest().body(new ErrorEntity(exception.getClass(), exception.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity constraintViolation(HttpServletRequest request, Exception exception) {
        return ResponseEntity.badRequest().body(new ErrorEntity(exception.getClass(), 
                parseConstraintViolationExceptionMessages((ConstraintViolationException)exception))
        );
    }

    private static List<String> parseConstraintViolationExceptionMessages(ConstraintViolationException exception) {
        List<String> messages = new ArrayList<>();
        exception.getConstraintViolations().forEach(constraintViolation -> {
            messages.add(constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage());
        });
        return messages;
    }
}
