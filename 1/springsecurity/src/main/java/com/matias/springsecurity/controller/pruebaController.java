package com.matias.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class pruebaController {

    @GetMapping("/holaseg")
    public String segHelloWorld() {
        return "hola mundo segurizado";
    }

    @GetMapping("/holanoseg")
    public String noSegHelloWorld() {
        return "hola mundo no segurizado";
    }

}

