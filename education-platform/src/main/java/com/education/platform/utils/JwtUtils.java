package com.education.platform.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    //insert two values from app properties
    @Value("${spring.jwt.security.private.key}")
    private String privateKey;

    @Value("${spring.jwt.user.generator}")
    private String userGenerator;

    //generation
    public String generateToken(Authentication authentication) {

        //First create the algorithm from the firm
        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

        //find the attributes from the authentication
        String username =  authentication.getPrincipal().toString();

        //find all authorities
        String authorities = authentication.getAuthorities()
                                            .stream()
                                            .map(GrantedAuthority::getAuthority)
                                            .collect(Collectors.joining(","));

        //generate token
        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date((System.currentTimeMillis())))
                .sign(algorithm);

        return jwtToken;
    }

    //validation
    public DecodedJWT validateToken(String token) {

        try{

            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

            JWTVerifier verifier = JWT.require( algorithm)
                                      .withIssuer(this.userGenerator)
                                      .build();

            DecodedJWT decodedJWT = verifier.verify(token);

            return decodedJWT;
        }
        catch (JWTVerificationException exception){
            throw new JWTVerificationException("Invalid Token, not authorize");
        }
    }

    //get username by jwtToken
    public String getUsername (DecodedJWT decodedJWT){
        return decodedJWT.getSubject().toString();
    }

    //get claim
    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claim){
        return decodedJWT.getClaim(claim);
    }

    //get all claims
    public Map<String,Claim> getAllClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }

}
