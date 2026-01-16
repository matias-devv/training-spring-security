package com.matias.springsecurity.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/*
cuando creamos una clase como registro, el compilador de java genera automaticamente metodos
como constructor, equals(), hashCode(), y toString(), basados en componentes de datos
declarados en la clase.
*/
@JsonPropertyOrder({"username","message","token","status"})
public record AuthResponseDTO(String username, String message, String token, boolean status ) {
}
