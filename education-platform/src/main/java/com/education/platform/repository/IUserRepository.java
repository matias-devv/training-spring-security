package com.education.platform.repository;

import com.education.platform.model.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserSec, Long> {

    Optional<UserSec> findEntityByUsername(String username);

    Optional<UserSec> findEntityById(Long id);

}
