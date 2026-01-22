package com.education.platform.controller;

import com.education.platform.dto.StudentDTO;
import com.education.platform.model.Student;
import com.education.platform.service.interfaces.IStudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@PreAuthorize("denyAll()")
public class StudentController {

    private final IStudentService studentService;

    public StudentController(IStudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/create")
    @PreAuthorize("permitAll()")
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO){

        Optional<StudentDTO> dto = studentService.createStudent(studentDTO);
        return dto.map(ResponseEntity::ok).orElseGet( ()-> ResponseEntity.notFound().build() );
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<StudentDTO> findById(@PathVariable Long id){

        Optional<StudentDTO> dto = studentService.findById(id);
        return dto.map(ResponseEntity::ok).orElseGet( ()-> ResponseEntity.notFound().build() );
    }

    @GetMapping("/find-all")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List> findAllStudents(){

        List<StudentDTO> listDto = studentService.findAllStudents();
        return ResponseEntity.ok(listDto);
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("permitAll()")
    public String deleteById(@PathVariable Long id){
        return studentService.deleteById(id);
    }

    @PutMapping("/update")
    @PreAuthorize("permitAll()")
    public ResponseEntity<StudentDTO> updateStudent(@RequestBody StudentDTO studentDTO){

        Optional<StudentDTO> dto = studentService.updateStudent(studentDTO);
        return dto.map(ResponseEntity::ok).orElseGet( ()-> ResponseEntity.notFound().build() );
    }

}
