package com.xiaoslab.coffee.api.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ErrorEntity {

    private Class exceptionClass = Exception.class;
    private List<String> messages = new ArrayList<>();

    public List<String> getMessages() {
        return this.messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    @JsonIgnore
    public Class getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(Class exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public String getException() {
        return exceptionClass.getSimpleName();
    }

    public ErrorEntity(String... messages) {
        Collections.addAll(this.messages, messages);
    }

    public ErrorEntity(List<String> messages) {
        this.messages = messages;
    }

    public ErrorEntity(Class exceptionClass, String... messages) {
        this.exceptionClass = exceptionClass;
        Collections.addAll(this.messages, messages);
    }

    public ErrorEntity(Class exceptionClass, List<String> messages) {
        this.exceptionClass = exceptionClass;
        this.messages = messages;
    }

}

