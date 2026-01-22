package com.education.platform.service.interfaces;

import com.education.platform.dto.UserDTO;
import com.education.platform.model.Professor;
import com.education.platform.model.Student;
import com.education.platform.model.UserSec;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    public Optional<UserDTO> createUser(UserDTO userDTO);

    public Optional<UserSec> findEntityById(Long id);

    public Optional<UserDTO> findById(Long id);

    public UserSec convertDtoToEntity( UserDTO userDTO);

    public UserSec verifyIfUserExist(Long id_user);

    public List<UserDTO> findAll();

    public Optional<UserDTO> updateUser(UserDTO userDTO);

    public void asignStudent(Student student, Long id_user);

    public void unlinkStudent(Long id_user);

    public void asignProfessor(Professor professor, Long id_user);

    public void unlinkProfessor(Long id_user);
}
