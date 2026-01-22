package com.education.platform.service.interfaces;

import com.education.platform.dto.PermissionDTO;
import com.education.platform.model.Permission;

import java.util.List;
import java.util.Optional;

public interface IPermissionService {

    public Optional<PermissionDTO> savePermission(PermissionDTO dto);

    public Optional<Permission> findById(Long id);

    public Optional<Permission> findByName(String name);

    public List<PermissionDTO> findAll();

    public Optional<PermissionDTO> updatePermission(PermissionDTO dto);
}
