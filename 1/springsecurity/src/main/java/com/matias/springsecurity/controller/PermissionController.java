package com.matias.springsecurity.controller;

import com.matias.springsecurity.model.Permission;
import com.matias.springsecurity.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;


    @GetMapping("/find-all")
    public ResponseEntity<List> getAllPermissions(){
        List permissions = permissionService.findAll();
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id){
        Optional<Permission> permission = permissionService.findById(id);
        return permission.map( ResponseEntity :: ok).orElseGet( ()-> ResponseEntity.notFound().build() );
    }

    @PostMapping("/save")
    public ResponseEntity savePermission(@RequestBody Permission permission){
        Permission newPermission = permissionService.save(permission);
        return ResponseEntity.ok(newPermission);
    }
}
