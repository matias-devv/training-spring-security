package com.matias.springsecurity.controller;

import com.matias.springsecurity.model.Role;
import com.matias.springsecurity.model.UserSec;
import com.matias.springsecurity.service.IRoleService;
import com.matias.springsecurity.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("denyAll()")
public class UserController {

    @Autowired private IUserService iUserService;

    @Autowired private IRoleService roleService;

    @GetMapping("/find-all")
    @PreAuthorize("permitAll()")
    public ResponseEntity findAll(){
        List users = iUserService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity findById(@PathVariable Long id){
        Optional user = iUserService.findById(id);
        return (ResponseEntity) user.map( ResponseEntity::ok ).orElseGet( () -> ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity save(@RequestBody UserSec userSec){

        Set<Role> roles = new HashSet<>();
        Role role;

        //seteo la contraseña encriptada
        userSec.setPassword( iUserService.encriptPassword(userSec.getPassword() ) );

        for( Role r : userSec.getRolesList()){

            role = (Role) roleService.findById( r.getId()).orElse(null);

            if( role != null){
                roles.add(role);
            }
        }
        if( !userSec.getRolesList().isEmpty() ){

            userSec.setRolesList(roles);
            UserSec newUser = iUserService.save(userSec);
            return ResponseEntity.ok(newUser);
        }
        return null;
    }
}
