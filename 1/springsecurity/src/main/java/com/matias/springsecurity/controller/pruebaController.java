package com.matias.springsecurity.controller;

import jakarta.annotation.security.DenyAll;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("denyAll()")
public class pruebaController {

    @GetMapping("/holaseg")
    @PreAuthorize("hasAuthority('READ')")
    public String segHelloWorld() {
        return "hola mundo segurizado";
    }

    @GetMapping("/holanoseg")
    @PreAuthorize("permitAll()")
    public String noSegHelloWorld() {
        return "hola mundo no segurizado";
    }

}

