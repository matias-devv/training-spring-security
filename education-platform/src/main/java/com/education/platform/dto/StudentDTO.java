package com.education.platform.dto;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record StudentDTO(Long id_student, String firstName, String lastName,
                         @NotBlank LocalDate date_of_birth, String cellphone,
                         String major, String gender,
                         @NotBlank String email, @NotBlank String dni,
                         @NotBlank Long id_user) {
}