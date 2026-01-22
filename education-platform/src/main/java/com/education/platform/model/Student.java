package com.education.platform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate date_of_birth;
    private String cellphone;
    private String major;
    private String gender;
    private String email;

    @Column(unique = true)
    private String dni;

    @OneToOne
    @JoinColumn(name = "id_user")
    private UserSec user;
}
