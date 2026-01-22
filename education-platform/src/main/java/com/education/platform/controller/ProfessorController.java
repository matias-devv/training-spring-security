package com.education.platform.controller;


import com.education.platform.dto.ProfessorDTO;
import com.education.platform.model.Professor;
import com.education.platform.service.interfaces.IProfessorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    private final IProfessorService professorService;

    public ProfessorController(IProfessorService professorService) {
        this.professorService = professorService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ProfessorDTO> createProfessor(@RequestBody ProfessorDTO professorDTO){

        Optional<ProfessorDTO> dto = professorService.createProfessor(professorDTO);
        return dto.map(ResponseEntity::ok).orElseGet( () -> ResponseEntity.badRequest().build() );
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ProfessorDTO> findById(@PathVariable Long id){

        Optional<ProfessorDTO> dto = professorService.findById(id);
        return dto.map(ResponseEntity::ok).orElseGet( ()-> ResponseEntity.notFound().build() );
    }

    @GetMapping("/find-all")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List> findAllProfessors(){

        List<ProfessorDTO> dtos = professorService.findAllProfessors();
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String deleteById(@PathVariable Long id){
        return professorService.deleteById(id);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ProfessorDTO> updateProfessor(@RequestBody ProfessorDTO professorDTO){

        Optional<ProfessorDTO> dto = professorService.updateProfessor(professorDTO);
        return dto.map(ResponseEntity::ok).orElseGet( ()-> ResponseEntity.notFound().build() );
    }
}
