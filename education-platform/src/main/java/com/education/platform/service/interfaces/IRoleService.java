package com.education.platform.service.interfaces;

import com.education.platform.dto.RoleDTO;
import com.education.platform.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {

    public Optional<RoleDTO> createRole(RoleDTO roleDTO);

    public Optional<Role> findById(Long id);

    public Optional<Role> findByName(String roleName);

    public List<RoleDTO> findAll();

    public Optional<RoleDTO> updateRole(RoleDTO roleDTO);
}
