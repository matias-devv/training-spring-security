package com.education.platform.repository;

import com.education.platform.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IStudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByIdIn(List<Long> idsStudents);
}
