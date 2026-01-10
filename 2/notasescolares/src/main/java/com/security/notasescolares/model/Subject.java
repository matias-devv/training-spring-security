package com.security.notasescolares.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Subject {

    private Long id;
    private String name;
    private Double calification;
    private Long alumn_id;
}
