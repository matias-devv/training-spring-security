package com.matias.springsecurity.controller;

import com.matias.springsecurity.model.Permission;
import com.matias.springsecurity.model.Role;
import com.matias.springsecurity.service.IPermissionService;
import com.matias.springsecurity.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPermissionService permissionService;

    @GetMapping("find-all")
    public ResponseEntity<List> getAllRoles(){
        List roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity getRoleById(@PathVariable Long id){
        Optional role = roleService.findById(id);
        return (ResponseEntity) role.map( ResponseEntity :: ok).orElseGet( ()-> ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity saveRole(@RequestBody Role role){

        Set<Permission> permissionsList = new HashSet<>();

        Permission readPermission;

        for ( Permission perm : role.getPermissionsList()){

            readPermission = (Permission) permissionService.findById( perm.getId()).orElse(null);

            if(readPermission != null){
                permissionsList.add(readPermission);
            }
        }
        role.setPermissionsList(permissionsList);
        Role newRole = roleService.save(role);
        return ResponseEntity.ok(newRole);
    }
}
