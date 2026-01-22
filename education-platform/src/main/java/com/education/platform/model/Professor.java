package com.education.platform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "professors")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String speciality;
    private Integer ages_experience;

    @Column(unique = true)
    private String dni;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.REMOVE)
    private List<Course> courses;

    @OneToOne
    @JoinColumn(name = "id_user")
    private UserSec user;
}
