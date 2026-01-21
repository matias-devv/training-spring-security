package com.education.platform.service;

import com.education.platform.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    public Optional<UserDTO> createUser(UserDTO userDTO);

    public Optional<UserDTO> findById(Long id);

    public List<UserDTO> findAll();

    public Optional<UserDTO> updateUser(UserDTO userDTO);

}
