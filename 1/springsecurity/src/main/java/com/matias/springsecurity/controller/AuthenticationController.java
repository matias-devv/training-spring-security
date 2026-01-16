package com.matias.springsecurity.controller;

import com.matias.springsecurity.dto.AuthLoginRequestDTO;
import com.matias.springsecurity.dto.AuthResponseDTO;
import com.matias.springsecurity.service.UserDetailsServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@PreAuthorize("permitAll()")
public class AuthenticationController {

    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    //todas estas request y responses vamos a tratarlas como DTO
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthLoginRequestDTO userRequest){
        return new ResponseEntity<>( this.userDetailsServiceImp.loginUser(userRequest), HttpStatus.OK);
    }
}
