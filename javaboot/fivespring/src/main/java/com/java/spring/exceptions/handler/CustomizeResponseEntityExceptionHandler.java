package com.java.spring.exceptions.handler;


import com.java.spring.exceptions.ExceptioResponse;
import com.java.spring.exceptions.InvalidJwtAuthenticationException;
import com.java.spring.exceptions.RequireObjectIsNullException;
import com.java.spring.exceptions.ResourceNotFounException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;


@ControllerAdvice
@RestController
public class CustomizeResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptioResponse> handlerAllExceptions(Exception ex, WebRequest request){
        ExceptioResponse exceptioResponse = new ExceptioResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(exceptioResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFounException.class)
    public final ResponseEntity<ExceptioResponse> handlerNotFoundExceptions(Exception ex, WebRequest request){
        ExceptioResponse exceptioResponse = new ExceptioResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(exceptioResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(RequireObjectIsNullException.class)
    public final ResponseEntity<ExceptioResponse> handlerBadRequestExceptions(Exception ex, WebRequest request){
        ExceptioResponse exceptioResponse = new ExceptioResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(exceptioResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public final ResponseEntity<ExceptioResponse> handlerInvalidaJwtAuthenticationExceptions(Exception ex, WebRequest request){
        ExceptioResponse exceptioResponse = new ExceptioResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(exceptioResponse, HttpStatus.FORBIDDEN);
    }
}
