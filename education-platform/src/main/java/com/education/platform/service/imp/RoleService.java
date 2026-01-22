package com.education.platform.service.imp;

import com.education.platform.dto.RoleDTO;
import com.education.platform.model.Permission;
import com.education.platform.model.Role;
import com.education.platform.repository.IRoleRepository;
import com.education.platform.service.interfaces.IPermissionService;
import com.education.platform.service.interfaces.IRoleService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleService implements IRoleService {

    private final IRoleRepository iRoleRepository;

    private final IPermissionService iPermissionService;

    public RoleService (IRoleRepository iRoleRepository, IPermissionService iPermissionService) {
        this.iRoleRepository = iRoleRepository;
        this.iPermissionService = iPermissionService;
    }

    @Override
    public Optional<RoleDTO> createRole(RoleDTO dto) {

        Set<Permission> permissionList = new HashSet<>();
        Optional<Permission> readPermission;

        for( Permission per : dto.permissionsList() ){

            //I need to search by id
            readPermission = iPermissionService.findById( per.getId() );

            if( readPermission.isPresent() ){
                permissionList.add( readPermission.get() );
            }
        }

        Role role = this.convertDtoToEntity(dto);

        role.setPermissionsList(permissionList);

        iRoleRepository.save(role);

        RoleDTO newDto = this.convertEntityToDTO(role);

        return Optional.of(newDto);
    }

    private Role convertDtoToEntity(RoleDTO dto) {
        Role role = new Role();
        role.setRoleName(dto.roleName());
        role.setPermissionsList(dto.permissionsList());
        return role;
    }

    @Override
    public Optional<Role> findById(Long id) {

        return iRoleRepository.findEntityById(id);

    }

    private RoleDTO convertEntityToDTO(Role role) {
        return new RoleDTO(role.getId(),
                           role.getRoleName(),
                           role.getPermissionsList());
    }


    @Override
    public Optional<Role> findByName(String roleName) {
        return iRoleRepository.findEntityByRoleName(roleName);
    }

    @Override
    public List<RoleDTO> findAll() {

        List<Role> list = iRoleRepository.findAll();

        List<RoleDTO> dtoList =  new ArrayList<>();

        list.forEach( role -> dtoList.add( this.convertEntityToDTO(role) ) );

        return dtoList;
    }

    @Override
    public Optional<RoleDTO> updateRole(RoleDTO roleDTO) {

        Optional<Role> role = iRoleRepository.findEntityById(roleDTO.id());

        if (role.isPresent()){

            Set<Permission> permissionList = new HashSet<>();

            for( Permission per : roleDTO.permissionsList() ) {

                Optional<Permission> permissionRead = iPermissionService.findById(per.getId());

                if (permissionRead.isPresent()) {

                    Permission permissionFound = permissionRead.get();
                    permissionList.add(permissionFound);
                }
            }

            role.get().setPermissionsList(permissionList);

            iRoleRepository.save( role.get() );

            RoleDTO newDto = this.convertEntityToDTO( role.get() );

            return Optional.of(newDto);
        }
        return Optional.empty();
    }


}
