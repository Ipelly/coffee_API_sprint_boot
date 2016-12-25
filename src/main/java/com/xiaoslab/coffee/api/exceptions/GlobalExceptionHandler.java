package com.xiaoslab.coffee.api.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotSupportedException;
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

    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity entityNotFound(HttpServletRequest request, Exception exception) {
        return ResponseEntity.notFound().build();
    }

    @ResponseBody
    @ExceptionHandler(NotSupportedException.class)
    public ResponseEntity notSupported(HttpServletRequest request, Exception exception) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
    }

    @ResponseBody
    @ExceptionHandler(NotAllowedException.class)
    public ResponseEntity notAllowed(HttpServletRequest request, Exception exception) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    //NotSupportedException
    private static List<String> parseConstraintViolationExceptionMessages(ConstraintViolationException exception) {
        List<String> messages = new ArrayList<>();
        exception.getConstraintViolations().forEach(constraintViolation -> {
            messages.add(constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage());
        });
        return messages;
    }
}
