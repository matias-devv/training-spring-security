package com.matias.springsecurity.service;

import com.matias.springsecurity.dto.AuthLoginRequestDTO;
import com.matias.springsecurity.dto.AuthResponseDTO;
import com.matias.springsecurity.model.UserSec;
import com.matias.springsecurity.repository.IUserRepository;
import com.matias.springsecurity.security.config.SecurityConfig;
import com.matias.springsecurity.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SecurityConfig securityConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //tenemos que usar SET y necesito devolver UserDetails
        //traigo el usuario de la BD
        UserSec userSec = null;
        try {
            userSec = (UserSec) userRepository.findUserEntityByUsername(username)
                    .orElseThrow( () -> new UsernameNotFoundException("The user " + username + "was not found"));

            //GrantedAuthority de Spring Security maneja permisos
            List<GrantedAuthority> authorityList = new ArrayList<>();

            //tomamos cada role y lo convertimos en SimpleGrantedAuthority para poder agregarlos a la authorityList
             userSec.getRolesList()
                    .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));

            //ahora agrego permisos
            userSec.getRolesList().stream()
                    .flatMap(role -> role.getPermissionsList().stream())//recorro permisos de roles
                    .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName())));

            return new User(userSec.getUsername(),
                            userSec.getPassword(),
                            userSec.isEnabled(),
                            userSec.isAccountNotExpired(),
                            userSec.isCredentialNotExpired(),
                            userSec.isAccountNotLocked(),
                            authorityList);

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public AuthResponseDTO loginUser(AuthLoginRequestDTO authLoginRequestDTO){

        //recupero nombre de usuario y contraseña
        String username = authLoginRequestDTO.username();

        String password = authLoginRequestDTO.password();

        Authentication authentication = this.autenticate(username, password);

        //si sale todo bien
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        AuthResponseDTO authResponseDTO = new AuthResponseDTO(username, "login ok", accessToken, true);

        return authResponseDTO;
    }

    private Authentication autenticate(String username, String password) {

        UserDetails userDetails = this.loadUserByUsername(username);

        if( userDetails == null ) {
            throw new BadCredentialsException("Invalid username or password.");
        }
        if( !securityConfig.passwordEncoder().matches( password, userDetails.getPassword() ) ) {
            throw new BadCredentialsException("Invalid username or password.");
        }
        return new UsernamePasswordAuthenticationToken( userDetails, password, userDetails.getAuthorities());
    }
}
