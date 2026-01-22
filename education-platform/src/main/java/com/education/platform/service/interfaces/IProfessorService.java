package com.education.platform.service.interfaces;

import com.education.platform.dto.ProfessorDTO;
import com.education.platform.model.Course;
import com.education.platform.model.Professor;

import java.util.List;
import java.util.Optional;

public interface IProfessorService {

    public Optional<ProfessorDTO> createProfessor(ProfessorDTO professorDTO);

    public Optional<ProfessorDTO> findById(Long id);

    public List<ProfessorDTO> findAllProfessors();

    public String deleteById(Long id);

    public Optional<ProfessorDTO> updateProfessor(ProfessorDTO professorDTO);

    public Professor convertDtoToEntity(ProfessorDTO professorDTO);

    public void linkCourseToProfessor(Course course, Long id_professor);

    public Professor findEntityById(Long id_professor);
}
