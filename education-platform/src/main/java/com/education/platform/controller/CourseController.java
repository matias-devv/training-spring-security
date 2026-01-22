package com.education.platform.controller;


import com.education.platform.dto.CourseDTO;
import com.education.platform.service.interfaces.ICourseService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final ICourseService courseService;

    public CourseController(ICourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/create")
    public ResponseEntity<CourseDTO> createCourse( @RequestBody CourseDTO courseDTO){

        Optional<CourseDTO> dto = courseService.createCourse(courseDTO);
        return dto.map(ResponseEntity::ok).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<CourseDTO> findById( @PathVariable Long id){

        Optional<CourseDTO> dto = courseService.findById(id);
        return dto.map(ResponseEntity::ok).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @GetMapping("/find-all")
    public ResponseEntity<List> findAllCourses(){

        List<CourseDTO> dtoList = courseService.findAllCourses();
        return ResponseEntity.ok(dtoList);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteById( @PathVariable Long id){
        return courseService.deleteById(id);
    }

    @PutMapping("/update")
    public ResponseEntity<CourseDTO> updateCourse(@RequestBody CourseDTO courseDTO){

        Optional<CourseDTO> dto = courseService.updateCourse(courseDTO);
        return dto.map(ResponseEntity::ok).orElseGet( () -> ResponseEntity.notFound().build() );
    }
}
