package com.education.platform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity @AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Table(name = "roles")
public class Role{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    String roleName;

    @ManyToMany( fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable ( name = "roles_permissions", joinColumns =  @JoinColumn ( name = "id_roles"),
                 inverseJoinColumns = @JoinColumn (name = "id_permissions") )
    private Set<Permission> permissionsList = new HashSet<>();
}
