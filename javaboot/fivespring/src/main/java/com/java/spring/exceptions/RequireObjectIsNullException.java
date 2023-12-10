package com.java.spring.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequireObjectIsNullException extends RuntimeException{


    private static final long serialVersionUID = 1L;

    public RequireObjectIsNullException() {
        super("Não é permitido a persistência de um objeto nulo");

    }

    public RequireObjectIsNullException(String message) {
        super(message);

    }


}
