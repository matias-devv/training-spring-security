package com.education.platform.repository;

import com.education.platform.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findEntityByRoleName(String roleName);

    Optional<Role> findEntityById(Long id);
}
