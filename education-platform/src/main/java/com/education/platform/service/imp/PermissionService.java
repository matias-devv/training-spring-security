package com.education.platform.service.imp;

import com.education.platform.dto.PermissionDTO;
import com.education.platform.model.Permission;
import com.education.platform.repository.IPermissionRepository;
import com.education.platform.service.interfaces.IPermissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionService implements IPermissionService {

    private final IPermissionRepository permissionRepository;

    public PermissionService(IPermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Optional<PermissionDTO> savePermission(PermissionDTO dto) {

        Permission permission = this.convertDtoToEntity(dto);

        permissionRepository.save(permission);

        PermissionDTO permissionDTO = this.convertEntityToDto(permission);

        return Optional.of(permissionDTO);

    }

    private Permission convertDtoToEntity(PermissionDTO dto) {
        Permission permission = new Permission();
        permission.setPermissionName(dto.permissionName());
        return permission;
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionRepository.findEntityById(id);
    }

    private PermissionDTO convertEntityToDto(Permission permissionEntity) {
        return new PermissionDTO(permissionEntity.getId(),permissionEntity.getPermissionName());
    }

    @Override
    public Optional<Permission> findByName(String name) {

        return permissionRepository.findEntityByPermissionName(name);
    }

    @Override
    public List<PermissionDTO> findAll() {

        List<Permission> permissionsList = permissionRepository.findAll();
        List<PermissionDTO> dtoList = new ArrayList<>();

        permissionsList.forEach(permission -> dtoList.add( this.convertEntityToDto(permission) ));

        return dtoList;
    }

    @Override
    public Optional<PermissionDTO> updatePermission(PermissionDTO dto) {

        Optional<Permission> permissionRead = permissionRepository.findById( dto.id() );

        if (permissionRead.isPresent()) {

            Permission permissionEntity = permissionRead.get();

            permissionEntity.setPermissionName(dto.permissionName());

            permissionRepository.save(permissionEntity);

            PermissionDTO newDto = this.convertEntityToDto(permissionEntity);

            return Optional.of(newDto);
        }
        return Optional.empty();
    }
}
