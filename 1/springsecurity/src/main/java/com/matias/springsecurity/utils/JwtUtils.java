package com.matias.springsecurity.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${security.jwt.private.key}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

                                //chequear si esta bien d donde importamos
    public String createToken(Authentication authentication){

        Algorithm algorithm = Algorithm.HMAC256(privateKey);

        //esto esta dentro del security context Holder
        String username = authentication.getPrincipal().toString();

        //tambien tenemos los permisos/autorizaciones
        //la idea es traer los permisos separados por coma
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String jwtToken = JWT.create()
                .withIssuer( this.userGenerator) // user que genera el token (yo)
                .withSubject( username) // a quien se le genera el token
                .withClaim("authorities", authorities) // claims -> datos contenidos dentro del JWT
                .withIssuedAt( new Date()) // fecha de generacion
                .withExpiresAt( new Date( System.currentTimeMillis() + 1800000)) // fecha expiracion
                .withJWTId(UUID.randomUUID().toString()) // id al token -> genera ramdom
                .withNotBefore( new Date( System.currentTimeMillis())) // desde cuando es valido( ahora en este caso)
                .sign(algorithm); //firma con la que creamos con la clave privada

        return jwtToken;
    }

    public DecodedJWT validateToken (String token){

        try{
            Algorithm algorithm = Algorithm.HMAC256(privateKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer( this.userGenerator)
                    .build();
            //si esta ok -> no genera exception y retorna
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        }
        catch (JWTVerificationException exception){
            throw new JWTVerificationException("Invalid token");
        }
    }

    public String extractUsername(DecodedJWT decodedJWT){
        //el subject es el usuario segun establecimos al crear el token
        return decodedJWT.getSubject().toString();
    }

    public Claim getSpecificClaim (DecodedJWT decodedJWT, String claimName){
        return decodedJWT.getClaim(claimName);
    }

    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }

}
