package com.education.platform.service.interfaces;

import com.education.platform.dto.StudentDTO;
import com.education.platform.model.Student;

import java.util.List;
import java.util.Optional;

public interface IStudentService {

    public Optional<StudentDTO> createStudent(StudentDTO studentDTO);

    public Optional<StudentDTO> findById(Long id);

    public List<StudentDTO> findAllStudents();

    List<Student> findStudentsByIds(List<Long> ids_students);

    public String deleteById(Long id);

    public Optional<StudentDTO> updateStudent(StudentDTO studentDTO);

}
