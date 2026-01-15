package com.matias.springsecurity.service;

import com.matias.springsecurity.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {

    List findAll();

    Optional findById(Long id);

    Role save(Role role);

    void deleteById(Long id);

    Role update(Role role);
}
