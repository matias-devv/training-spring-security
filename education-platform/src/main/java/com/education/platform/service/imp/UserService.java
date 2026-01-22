package com.education.platform.service.imp;

import com.education.platform.dto.UserDTO;
import com.education.platform.model.Professor;
import com.education.platform.model.Role;
import com.education.platform.model.Student;
import com.education.platform.model.UserSec;
import com.education.platform.repository.IUserRepository;
import com.education.platform.security.config.SecurityConfig;
import com.education.platform.service.interfaces.IRoleService;
import com.education.platform.service.interfaces.IUserService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    private final IRoleService roleService;

    private final SecurityConfig securityConfig;

    public UserService(IUserRepository userRepository, IRoleService roleService, SecurityConfig securityConfig) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.securityConfig = securityConfig;
    }

    @Override
    public Optional<UserDTO> createUser(UserDTO dto) {

        UserSec newUser = new UserSec();

        newUser = this.convertDtoToEntity( dto);

        Set<Role> roles = this.getRolesList(dto);

        newUser.setListRoles(roles);

        newUser.setPassword(securityConfig.bCryptPasswordEncoder().encode(dto.password()));

        userRepository.save(newUser);

        return Optional.of(convertEntityToDTO(newUser));
    }

    private Set<Role> getRolesList(UserDTO dto) {
        Set<Role> roles = new HashSet<>();
        dto.listRoles().forEach(role -> { roleService.findById( role.getId() ).ifPresent( roles::add ); });
        return roles;
    }

    @Override
    public UserSec convertDtoToEntity(UserDTO dto) {
        UserSec user = new UserSec();
        user.setId( dto.id() );
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setEnabled(dto.enabled());
        user.setAccountNotExpired(dto.accountNotExpired());
        user.setAccountNotLocked(dto.accountNotLocked());
        user.setCredentialsNotExpired(dto.credentialsNotExpired());
        user.setListRoles(dto.listRoles());
        return user;
    }

    @Override
    public UserSec verifyIfUserExist(Long id_user) {
        return userRepository.findById(id_user).orElse(null);
    }

    @Override
    public Optional<UserDTO> findById(Long id) {

        Optional<UserSec> userSec = userRepository.findById(id);

        if( userSec.isPresent()) {

            UserSec user = userSec.get();

            UserDTO dto = this.convertEntityToDTO(user);

            return Optional.of(dto);

        }
        return Optional.empty();
    }

    @Override
    public Optional<UserSec> findEntityById(Long id) {

        UserSec userSec = userRepository.findById(id).orElse(null);

        if( userSec != null){
            return Optional.of(userSec);
        }
        return Optional.empty();
    }

    private UserDTO convertEntityToDTO(UserSec user) {
            return new UserDTO(user.getId(),
                                  user.getUsername(),
                                  user.getPassword(),
                                  user.isEnabled(),
                                  user.isAccountNotExpired(),
                                  user.isAccountNotLocked(),
                                  user.isCredentialsNotExpired(),
                                  user.getListRoles());
    }

    @Override
    public List<UserDTO> findAll() {

        List<UserSec> usersList = userRepository.findAll();

        List<UserDTO> dtoList = new ArrayList<>();

        usersList.forEach(user -> dtoList.add( this.convertEntityToDTO(user) ) );

        return dtoList;
    }


    @Override
    public Optional<UserDTO> updateUser(UserDTO dto) {

        Optional <UserSec> userRead = userRepository.findEntityByUsername( dto.username() );

        if( userRead.isPresent() ) {

            UserSec user = userRead.get();

            user.setPassword( securityConfig.bCryptPasswordEncoder().encode( dto.password() ));
            user.setEnabled(dto.enabled());
            user.setAccountNotExpired(dto.accountNotExpired());
            user.setAccountNotLocked(dto.accountNotLocked());
            user.setCredentialsNotExpired(dto.credentialsNotExpired());

            Set<Role> rolesList = this.getRolesList(dto);

            if ( !rolesList.isEmpty()) {

                user.setListRoles(rolesList);

                userRepository.save(user);

                UserDTO newDTO = this.convertEntityToDTO(user);

                return Optional.of(newDTO);
            }
        }
        return Optional.empty();
    }

    @Override
    public void asignStudent(Student student, Long id_user) {
        Optional<UserSec> user = userRepository.findEntityById( id_user );

        if ( user.isPresent() ) {

            UserSec userEntity = user.get();

            userEntity.setStudent( student );

            userRepository.save(userEntity);
        }
    }

    @Override
    public void unlinkStudent(Long id_user) {

        Optional<UserSec> user = userRepository.findEntityById( id_user );

        if ( user.isPresent() ) {

            UserSec userEntity = user.get();

            userEntity.setStudent( null );

            userRepository.save(userEntity);
        }
    }

    @Override
    public void asignProfessor(Professor professor, Long id_user) {

        Optional<UserSec> user = userRepository.findEntityById( id_user );

        if ( user.isPresent() ) {

            UserSec userEntity = user.get();

            userEntity.setProfessor( professor );

            userRepository.save(userEntity);
        }
    }

    @Override
    public void unlinkProfessor(Long id_user) {

        Optional<UserSec> user = userRepository.findEntityById( id_user );

        if ( user.isPresent() ) {

            UserSec userEntity = user.get();

            userEntity.setProfessor( null );

            userRepository.save(userEntity);
        }
    }

}
