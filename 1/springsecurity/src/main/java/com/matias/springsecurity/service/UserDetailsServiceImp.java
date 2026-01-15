package com.matias.springsecurity.service;

import com.matias.springsecurity.model.UserSec;
import com.matias.springsecurity.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //tenemos que usar SET y necesito devolver UserDetails
        //traigo el usuario de la BD
        UserSec userSec = null;
        try {
            userSec = (UserSec) userRepository.findUserEntityByUsername(username)
                    .orElseThrow( () -> new UsernameNotFoundException("The user " + username + "was not found"));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

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
    }
}
