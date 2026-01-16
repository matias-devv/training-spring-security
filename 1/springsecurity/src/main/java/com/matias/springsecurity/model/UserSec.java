package com.matias.springsecurity.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity @Getter @Setter @AllArgsConstructor
@NoArgsConstructor
@Table ( name = "users")
public class UserSec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private boolean enabled;
    private boolean accountNotExpired;
    private boolean accountNotLocked;
    private boolean credentialNotExpired;

    //uso SET pq no permite repetidos
    //LIST permite repetidos
    @ManyToMany( fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable( name = "user_roles", joinColumns = @JoinColumn( name = "user_id"),
                inverseJoinColumns = @JoinColumn( name = "role_id") )
    private Set<Role> rolesList = new HashSet<>();


}
