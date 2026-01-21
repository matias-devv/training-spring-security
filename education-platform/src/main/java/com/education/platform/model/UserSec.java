package com.education.platform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity @Getter @Setter @AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserSec {

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    private boolean enabled;
    private boolean accountNotExpired;
    private boolean accountNotLocked;
    private boolean credentialsNotExpired;


    @ManyToMany( fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable( name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
                  inverseJoinColumns = @JoinColumn (name = "role_id") )
    private Set<Role> listRoles = new HashSet<>();
}
