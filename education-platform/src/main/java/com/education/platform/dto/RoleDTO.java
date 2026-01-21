package com.education.platform.dto;

import com.education.platform.model.Permission;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record RoleDTO(Long id,
                      @NotBlank String roleName,
                      @NotBlank Set<Permission> permissionsList) {
}
