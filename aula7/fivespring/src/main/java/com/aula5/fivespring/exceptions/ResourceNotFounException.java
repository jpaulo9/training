package com.aula5.fivespring.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFounException extends RuntimeException{


    private static final long serialVersionUID = 1L;


    public ResourceNotFounException(String message) {
        super(message);
    }
}
