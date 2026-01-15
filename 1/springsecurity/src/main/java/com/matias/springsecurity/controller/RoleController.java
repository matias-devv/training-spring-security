package com.matias.springsecurity.controller;

import com.matias.springsecurity.model.Permission;
import com.matias.springsecurity.model.Role;
import com.matias.springsecurity.service.IPermissionService;
import com.matias.springsecurity.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@PreAuthorize("denyAll()")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @GetMapping("find-all")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List> getAllRoles(){
        List roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity getRoleById(@PathVariable Long id){
        Optional role = roleService.findById(id);
        return (ResponseEntity) role.map( ResponseEntity :: ok).orElseGet( ()-> ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity saveRole(@RequestBody Role role){

        Role newRole = roleService.save(role);
        
        return ResponseEntity.ok(newRole);
    }

    @PatchMapping("/patch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> patchRole(@RequestBody Role role){

        Role updatedRole = roleService.update(role);

        return ResponseEntity.ok(updatedRole);
    }
}
