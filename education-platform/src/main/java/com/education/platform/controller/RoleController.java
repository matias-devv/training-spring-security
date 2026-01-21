package com.education.platform.controller;

import com.education.platform.dto.RoleDTO;
import com.education.platform.model.Role;
import com.education.platform.service.IRoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@PreAuthorize("denyAll()")
public class RoleController {

    private final IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<RoleDTO> save(@RequestBody RoleDTO roleDTO) {

        Optional<RoleDTO> newRole = roleService.createRole(roleDTO);
        return newRole.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/find-by-id/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Role> findById(@PathVariable Long id) {

        Optional<Role> role = roleService.findById(id);
        return role.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/find-by-name/{name}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Role> findByName(@PathVariable String name) {

        Optional<Role> role = roleService.findByName(name);
        return role.map(ResponseEntity::ok).orElseGet( ()->ResponseEntity.notFound().build() );
    }

    @GetMapping("/find-all")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List> findAll() {

        List<RoleDTO> listDTOs = roleService.findAll();
        return ResponseEntity.ok(listDTOs);
    }

    @PatchMapping("/patch")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<RoleDTO> update(@RequestBody RoleDTO roleDTO) {

        Optional<RoleDTO> updatedRole = roleService.updateRole(roleDTO);
        return updatedRole.map(ResponseEntity::ok)
                          .orElseGet( ()-> ResponseEntity.notFound().build());
    }
}
