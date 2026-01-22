package com.education.platform.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CourseDTO(Long id, @NotBlank String courseName,
                                 @NotBlank List<Long> ids_students,
                                 @NotBlank Long id_professor) {
}

