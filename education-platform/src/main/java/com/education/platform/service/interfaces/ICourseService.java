package com.education.platform.service.interfaces;

import com.education.platform.dto.CourseDTO;
import com.education.platform.dto.ProfessorDTO;

import java.util.List;
import java.util.Optional;

public interface ICourseService {

    public Optional<CourseDTO> createCourse(CourseDTO courseDTO);

    public Optional<CourseDTO> findById(Long id);

    public List<CourseDTO> findAllCourses();

    public String deleteById(Long id);

    public Optional<CourseDTO> updateCourse(CourseDTO courseDTO);
}
