package com.java.spring.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.java.spring.data.vo.v1.security.TokenVO;
import com.java.spring.exceptions.InvalidJwtAuthenticationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.List;
import java.util.Date;

@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliSeconds = 3600000;


    @Autowired
    private UserDetailsService userDetailsService;

    Algorithm algorithm = null;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenVO createAccessToken (String userName, List<String> roles){
        Date now = new Date();
        Date validity = new Date(now.getTime()+validityInMilliSeconds);
        var accessToken = getAccessToken (userName, roles, now, validity);
        var refreshToken = getRefreshToken(userName, roles, now);
        System.out.println(" "+accessToken);
        return new TokenVO(userName, true, now, validity, accessToken, refreshToken);
    }

    public TokenVO refreshToken(String refrhoken){
        if(refrhoken.contains("Bearer")) refrhoken =
                refrhoken.substring("Bearer ".length());

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refrhoken);
        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        return createAccessToken(username, roles);
    }
    private String getAccessToken(String userName, List<String> roles, Date now, Date validity) {
        String issueUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath().build().toUriString();

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withSubject(userName)
                .withIssuer(issueUrl)
                .sign(algorithm)
                .strip();
    }


    private String getRefreshToken(String userName, List<String> roles, Date now) {

        Date validityRefreshToken = new Date(now.getTime() +(validityInMilliSeconds * 3));
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validityRefreshToken)
                .withSubject(userName)
                .sign(algorithm)
                .strip();
    }


    public Authentication getAuthentication(String token){
        DecodedJWT decodedJWT = decodedJWTToken(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedJWTToken(String token) {

        Algorithm alg =   Algorithm.HMAC256(secretKey.getBytes());

        JWTVerifier verifier = JWT.require(alg).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        System.out.println(" "+decodedJWT);
        return decodedJWT;
    }

    public String ResolveToken(HttpServletRequest req){

        String bearerToken = req.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring("Bearer ".length());
        }
        return null;

    }

    public boolean validateToken (String token){
        DecodedJWT decodedJWT = decodedJWTToken(token);
        try{

            if (decodedJWT.getExpiresAt().before(new Date())){
                return false;
            }

            return true;


        }catch (Exception ex){
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT Token!");
        }
    }


}
