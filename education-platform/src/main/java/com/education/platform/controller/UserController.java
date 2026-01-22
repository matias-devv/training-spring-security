package com.education.platform.controller;

import com.education.platform.dto.UserDTO;
import com.education.platform.service.interfaces.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("denyAll()")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) {

        Optional<UserDTO> dto = userService.createUser(userDTO);
        return dto.map(ResponseEntity::ok)
                  .orElseGet( ()-> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {

        Optional<UserDTO> dto = userService.findById(id);
        return dto.map(ResponseEntity::ok)
                  .orElseGet( ()-> ResponseEntity.notFound().build());
    }

    @GetMapping("/find-all")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List> findAll() {
        List<UserDTO> dtoList = userService.findAll();
        return ResponseEntity.ok(dtoList);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UserDTO> updateUser (@RequestBody UserDTO userDTO){

        Optional<UserDTO> dto =  userService.updateUser(userDTO);
        return dto.map(ResponseEntity::ok)
                  .orElseGet( ()-> ResponseEntity.notFound().build());
    }
}
