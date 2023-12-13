package com.java.spring.controllers;


import com.java.spring.data.vo.v1.security.AccountCredentialsVO;
import com.java.spring.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authservice;

    @Operation(summary = "Authenticates a user and returns a token")
    @PostMapping(value = "/signin")
    public ResponseEntity signin (@RequestBody AccountCredentialsVO data){
        if(checkIsParamIsNotNull(data)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request");
        }

        var token = authservice.signin(data);
        System.out.println(data.getPassword()+" "+data.getPassword());
        if (token==null )
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request");

        return token;


    }

    @SuppressWarnings("rawtypes")
    @Operation(summary = "Refresh token")
    @PutMapping (value = "/refresh/{username}")
    public ResponseEntity refreshToken (@PathVariable ("username") String username,
                                        @RequestHeader("Authorization") String refreshToken){

        if(checkIsParamIsNotNull(username, refreshToken)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request");
        }

        var token = authservice.refreshToken(username, refreshToken);

        if (token==null )
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request");

        return token;

    }
    private boolean checkIsParamIsNotNull(AccountCredentialsVO data){
        return (data == null || data.getUsername()==null || data.getUsername().isBlank()
                ||data.getPassword() == null || data.getPassword().isBlank());

    }

    private boolean checkIsParamIsNotNull(String userName, String refreshToken){
        return refreshToken == null || refreshToken.isBlank() ||
                userName == null || userName.isBlank();
    }

}
