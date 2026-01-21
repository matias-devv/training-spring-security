package com.education.platform.repository;

import com.education.platform.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findEntityById(Long id);

    Optional<Permission> findEntityByPermissionName(String name);
}
