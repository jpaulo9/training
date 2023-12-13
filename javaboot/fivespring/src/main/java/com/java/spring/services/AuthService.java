package com.java.spring.services;


import com.java.spring.data.vo.v1.security.AccountCredentialsVO;
import com.java.spring.data.vo.v1.security.TokenVO;
import com.java.spring.exceptions.InvalidJwtAuthenticationException;
import com.java.spring.repository.UserRepository;
import com.java.spring.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @SuppressWarnings("rawtypes")
    public ResponseEntity signin(AccountCredentialsVO data){

        try {

            var username= data.getUsername();
            var password = data.getPassword();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            var user = userRepository.findByUserName(username);
            var tokenReponse = new TokenVO();
            if(user!= null){
                tokenReponse = jwtTokenProvider.createAccessToken(username, user.getRoles());

            }else {
                throw new UsernameNotFoundException("UserName "+username+" not found!");
            }
            System.out.println(tokenReponse.getUsername().toString());
            return ResponseEntity.ok(tokenReponse);

        }catch (Exception ex){
            throw new BadCredentialsException("Invalid username/password supplied!"+ex.getMessage());
        }

    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity refreshToken(String username, String refreshtk){

            var user = userRepository.findByUserName(username);
            var tokenReponse = new TokenVO();
            if(user!= null){
                tokenReponse = jwtTokenProvider.refreshToken(refreshtk);

            }else {
                throw new UsernameNotFoundException("UserName "+username+" not found!");
            }
            System.out.println(tokenReponse.getUsername().toString());
            return ResponseEntity.ok(tokenReponse);


    }

}
