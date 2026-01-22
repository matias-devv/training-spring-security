package com.education.platform.repository;

import com.education.platform.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProfessorRepository extends JpaRepository<Professor, Long> {
}
