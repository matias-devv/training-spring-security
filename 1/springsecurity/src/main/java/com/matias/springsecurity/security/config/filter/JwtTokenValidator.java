package com.matias.springsecurity.security.config.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.matias.springsecurity.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

public class JwtTokenValidator extends OncePerRequestFilter {

    private JwtUtils jwtUtils;

    public JwtTokenValidator(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    //el nonNull tiene que ser de spring framework, no lombok
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (jwtToken != null) {
            //en el header antes del token viene el bearer ( esquema de auth) y tenemos que sacarlo
            jwtToken = jwtToken.substring(7); // son 7 letras + 1 espacio

            DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);

            //si es valido le damos acceso
            String username = jwtUtils.extractUsername(decodedJWT);

            //me devuelve claim, lo paso a String ( necesario)
            String authorities = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();

            //si esta todo ok hay que setearlo en el context holder, para eso tengo que convertirlos en GrantedAuthority
            Collection <? extends GrantedAuthority>authoritiesList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

            //si se valida ok el token le doy acceso al usuario en el context holder
            SecurityContext context = SecurityContextHolder.getContext();

            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authoritiesList);

            context.setAuthentication(authentication);

            SecurityContextHolder.setContext(context);
        }
        //si no tiene token -> esto arroja error
        filterChain.doFilter(request, response);
    }
}
