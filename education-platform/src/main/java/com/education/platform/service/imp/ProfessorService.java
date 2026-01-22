package com.education.platform.service.imp;

import com.education.platform.dto.ProfessorDTO;
import com.education.platform.model.Course;
import com.education.platform.model.Professor;
import com.education.platform.model.UserSec;
import com.education.platform.repository.IProfessorRepository;
import com.education.platform.service.interfaces.IProfessorService;
import com.education.platform.service.interfaces.IUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService implements IProfessorService {

    private final IProfessorRepository professorRepository;

    private final IUserService userService;

    public ProfessorService(IProfessorRepository professorRepository, IUserService userService) {
        this.professorRepository = professorRepository;
        this.userService = userService;
    }

    @Override
    public Optional<ProfessorDTO> createProfessor(ProfessorDTO professorDTO) {

        UserSec user = this.verifyIfExistsUser(professorDTO);

        Professor newProfessor = this.convertDtoToEntity(professorDTO);

        professorRepository.save( newProfessor );

        userService.asignProfessor( newProfessor, user.getId() );

        newProfessor = this.assignUserToProfessor( newProfessor, user);

        ProfessorDTO newDto = this.convertEntityToDto(newProfessor);

        return Optional.of(newDto);
    }

    private Professor assignUserToProfessor(Professor newProfessor, UserSec user) {

        newProfessor.setUser(user);

        professorRepository.save(newProfessor);

        return newProfessor;
    }

    private UserSec verifyIfExistsUser(ProfessorDTO professorDTO) {

        UserSec userRead = userService.verifyIfUserExist( professorDTO.id_user() );

        if ( userRead != null) {
            return userRead;
        }
        throw new RuntimeException("The user with that id was not found");
    }

    @Override
    public Professor convertDtoToEntity(ProfessorDTO professorDTO) throws RuntimeException {
        Professor newProfessor = new Professor();
        newProfessor.setFirstName(professorDTO.firstName());
        newProfessor.setLastName(professorDTO.lastName());
        newProfessor.setSpeciality(professorDTO.speciality());
        newProfessor.setAges_experience(professorDTO.ages_experience());
        newProfessor.setDni(professorDTO.dni());
        return newProfessor;
    }

    private ProfessorDTO convertEntityToDto(Professor professor) {

        if (professor.getCourses() != null) {

            List<Long> ids_courses = new ArrayList<>();

            professor.getCourses().forEach(course -> ids_courses.add(course.getId()));

            return new ProfessorDTO(professor.getId(),
                    professor.getFirstName(),
                    professor.getLastName(),
                    professor.getSpeciality(),
                    professor.getAges_experience(),
                    professor.getDni(),
                    ids_courses,
                    professor.getUser().getId());
        }
        return new ProfessorDTO(professor.getId(),
                professor.getFirstName(),
                professor.getLastName(),
                professor.getSpeciality(),
                professor.getAges_experience(),
                professor.getDni(),
                null,
                professor.getUser().getId());
    }

    @Override
    public Optional<ProfessorDTO> findById(Long id) {

        Optional<Professor> professor = professorRepository.findById(id);
        if (professor.isPresent() ) {

            ProfessorDTO professorDTO = this.convertEntityToDto( professor.get() );

            return Optional.of(professorDTO);
        }
        return Optional.empty();
    }

    @Override
    public List<ProfessorDTO> findAllProfessors() {

        List<Professor> professors = professorRepository.findAll();

        List<ProfessorDTO> dtos = new ArrayList<>();

        professors.forEach(professor -> { dtos.add( this.convertEntityToDto( professor ) ); } );

        return dtos;
    }

    @Override
    public String deleteById(Long id) {

        Professor professor = professorRepository.findById(id).orElse(null);

        if(professor != null){

            Optional<UserSec> userRead = userService.findEntityById( professor.getUser().getId() );

            if (userRead.isPresent()) {

                //set null on the "id_student" field on userEntity
                userService.unlinkProfessor( userRead.get().getId() );

                //delete student
                professor.setUser( null );

                professorRepository.deleteById(id);

                return "The professor was deleted successfully";
            }
        }
        return "The professor was not found";
    }

    @Override
    public Optional<ProfessorDTO> updateProfessor(ProfessorDTO professorDTO) {

        //buscar profesor
        Professor professor = professorRepository.findById( professorDTO.id_professor() ).orElse(null);

        if ( professor != null ){
  
            professor.setFirstName(professorDTO.firstName());
            professor.setLastName(professorDTO.lastName());
            professor.setSpeciality(professorDTO.speciality());
            professor.setAges_experience(professorDTO.ages_experience());
            professor.setDni(professorDTO.dni());

            professorRepository.save(professor);

            ProfessorDTO dto = convertEntityToDto(professor);

            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Override
    public void linkCourseToProfessor(Course course, Long id_professor) {

        Professor professorEntity =  professorRepository.findById(id_professor).orElse(null);

        if (professorEntity != null) {

            professorEntity.getCourses().add(course);
            professorRepository.save(professorEntity);
        }
    }

    @Override
    public Professor findEntityById(Long id_professor) {

        Professor professorEntity =  professorRepository.findById(id_professor).orElse(null);

        if (professorEntity != null) {
            return professorEntity;
        }
        return null;
    }
}
