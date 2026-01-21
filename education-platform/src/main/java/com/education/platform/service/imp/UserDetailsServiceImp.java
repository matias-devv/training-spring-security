package com.education.platform.service.imp;

import com.education.platform.dto.AuthLoginRequestDTO;
import com.education.platform.dto.AuthResponseDTO;
import com.education.platform.model.UserSec;
import com.education.platform.repository.IUserRepository;
import com.education.platform.security.config.SecurityConfig;
import com.education.platform.utils.JwtUtils;
import jakarta.validation.Valid;
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
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final IUserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final SecurityConfig securityConfig;

    public UserDetailsServiceImp(IUserRepository userRepository, JwtUtils jwtUtils, SecurityConfig securityConfig) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.securityConfig = securityConfig;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserSec user = (UserSec) userRepository.findEntityByUsername(username)
                                                .orElseThrow( () -> new UsernameNotFoundException( "The user with this: " + username + " was not found") );

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        //roles -> GrantedAuthority ( concat with "ROLE_" )
        user.getListRoles()
            .forEach(role -> {
                     authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleName() ) ) ); } );

        //permissions -> GrantedAuthority
        user.getListRoles().stream()
                .flatMap( role -> role.getPermissionsList().stream() )
                .forEach(permission -> { authorities
                                                   .add(new SimpleGrantedAuthority(permission.getPermissionName() ) ); } );

        //crear User de UserDetailsService
        return new User( user.getUsername(),
                         user.getPassword(),
                         user.isEnabled(),
                         user.isAccountNotExpired(),
                         user.isCredentialsNotExpired(),
                         user.isAccountNotLocked(),
                         authorities );
    }

    public AuthResponseDTO loginUser(@Valid AuthLoginRequestDTO userRequest) {

        String username = userRequest.username();
        String password = userRequest.password();

        Authentication authentication = this.authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.generateToken( authentication );

        return new AuthResponseDTO(username, "login ok", accessToken, true);
    }

    private Authentication authenticate(String username, String password) {

        //find user
        UserDetails userDetails = this.loadUserByUsername(username);

        if( userDetails == null){
            throw new UsernameNotFoundException("Invalid username or password");
        }
        if (!securityConfig.bCryptPasswordEncoder().matches(password, userDetails.getPassword()) ){
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken( username, userDetails.getPassword(), userDetails.getAuthorities() );
    }
}
