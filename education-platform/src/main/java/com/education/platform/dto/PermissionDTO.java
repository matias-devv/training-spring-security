package com.education.platform.dto;

import jakarta.validation.constraints.NotBlank;

public record PermissionDTO(Long id,
                            @NotBlank String permissionName) {
}
