package com.education.platform.controller;

import com.education.platform.dto.PermissionDTO;
import com.education.platform.model.Permission;
import com.education.platform.service.interfaces.IPermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/permissions")
@PreAuthorize("denyAll()")
public class PermissionController {

    private final IPermissionService permissionService;

    public PermissionController (IPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<PermissionDTO> savePermission(@RequestBody PermissionDTO permissionDTO) {

        Optional<PermissionDTO> permission = permissionService.savePermission(permissionDTO);
        return permission.map(ResponseEntity::ok)
                         .orElseGet( ()-> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(permissionDTO) );
    }

    @GetMapping("/find-by-id/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Permission> findById(@PathVariable Long id) {

        Optional<Permission> permission = permissionService.findById(id);
        return permission.map(ResponseEntity::ok)
                         .orElseGet( () -> ResponseEntity.notFound().build());
    }

    @GetMapping("/find-by-name/{name}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Permission> findByName(@PathVariable String name) {

        Optional<Permission> permission = permissionService.findByName(name);
        return permission.map(ResponseEntity::ok)
                         .orElseGet( ()-> ResponseEntity.notFound().build() );
    }

    @GetMapping("/find-all")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List> findAll() {
        return ResponseEntity.ok( permissionService.findAll() );
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<PermissionDTO> updatePermission(@RequestBody PermissionDTO permissionDTO) {

        Optional<PermissionDTO> permission = permissionService.updatePermission(permissionDTO);
        return permission.map(ResponseEntity::ok)
                         .orElseGet( () -> ResponseEntity.notFound().build());
    }
}
