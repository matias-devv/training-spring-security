package com.matias.springsecurity.service;

import com.matias.springsecurity.model.Permission;
import com.matias.springsecurity.model.Role;
import com.matias.springsecurity.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IPermissionService permissionService;

    @Override
    public List findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role save(Role role) {

        Set<Permission> permissionsList = new HashSet<>();

        Permission readPermission;

        for ( Permission perm : role.getPermissionsList()){

            readPermission = (Permission) permissionService.findById( perm.getId()).orElse(null);

            if(readPermission != null){
                permissionsList.add(readPermission);
            }
        }
        role.setPermissionsList(permissionsList);
        roleRepository.save(role);

        return roleRepository.findById(role.getId()).orElse(null);
    }


    @Override
    public Role update(Role role) {
        return this.save(role);
    }
}
