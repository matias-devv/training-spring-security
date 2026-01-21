package com.education.platform.dto;

import com.education.platform.model.Role;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UserDTO(Long id,
                      String username,
                      @NotBlank String password,
                      @NotBlank boolean enabled,
                      @NotBlank boolean accountNotExpired,
                      @NotBlank boolean accountNotLocked,
                      @NotBlank boolean credentialsNotExpired,
                      @NotBlank Set<Role> listRoles) {
}
