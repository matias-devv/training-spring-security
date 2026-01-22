package com.education.platform.dto;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ProfessorDTO(Long id_professor,
                           String firstName, String lastName,
                           String speciality, Integer ages_experience,
                           @NotBlank String dni, List<Long> ids_courses,
                           @NotBlank Long id_user) {
}

