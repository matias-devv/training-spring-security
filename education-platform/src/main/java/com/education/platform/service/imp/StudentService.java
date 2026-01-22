package com.education.platform.service.imp;

import com.education.platform.dto.StudentDTO;
import com.education.platform.dto.UserDTO;
import com.education.platform.model.Student;
import com.education.platform.model.UserSec;
import com.education.platform.repository.IStudentRepository;
import com.education.platform.service.interfaces.ICourseService;
import com.education.platform.service.interfaces.IStudentService;
import com.education.platform.service.interfaces.IUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService implements IStudentService {

    private final IStudentRepository studentRepository;

    private final IUserService userService;

    public StudentService(IStudentRepository studentRepository, IUserService userService) {
        this.studentRepository = studentRepository;
        this.userService = userService;
    }

    @Override
    public Optional<StudentDTO> createStudent(StudentDTO studentDTO) throws RuntimeException {

        UserSec user = this.verifyIfUserExists(studentDTO);

        Student newStudent = this.convertDtoToEntity(studentDTO);

        studentRepository.save(newStudent);

        userService.asignStudent(newStudent, user.getId());

        //asign user to this student
        newStudent = this.asignUserToStudent(user, newStudent);

        StudentDTO newDto = this.convertEntityToDto(newStudent);

        return Optional.of(newDto);
    }

    private Student asignUserToStudent(UserSec user, Student newStudent) {

        newStudent.setUser(user);

        studentRepository.save(newStudent);

        return newStudent;
    }

    private UserSec verifyIfUserExists(StudentDTO studentDTO) {

        UserSec userRead = userService.verifyIfUserExist(studentDTO.id_user());

        if (userRead != null) {
            return userRead;
        }
        throw new RuntimeException("The user with that id was not found");
    }

    private Student convertDtoToEntity(StudentDTO studentDTO) {
        Student newStudent = new Student();
        newStudent.setFirstName(studentDTO.firstName());
        newStudent.setLastName(studentDTO.lastName());
        newStudent.setDate_of_birth(studentDTO.date_of_birth());
        newStudent.setCellphone(studentDTO.cellphone());
        newStudent.setMajor(studentDTO.major());
        newStudent.setGender(studentDTO.gender());
        newStudent.setEmail(studentDTO.email());
        newStudent.setDni(studentDTO.dni());
        return newStudent;
    }

    private StudentDTO convertEntityToDto(Student student) {

        if (student.getUser() != null) {

            return new StudentDTO(
                    student.getId(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getDate_of_birth(),
                    student.getCellphone(),
                    student.getMajor(),
                    student.getGender(),
                    student.getEmail(),
                    student.getDni(),
                    student.getUser().getId());
        }
        return new StudentDTO(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getDate_of_birth(),
                student.getCellphone(),
                student.getMajor(),
                student.getGender(),
                student.getEmail(),
                student.getDni(),
                null);
    }

    @Override
    public Optional<StudentDTO> findById(Long id) {

        Student studentFound = studentRepository.findById(id)
                .orElse(null);
        if (studentFound != null) {

            StudentDTO newDto = this.convertEntityToDto(studentFound);
            return Optional.of(newDto);
        }
        return Optional.empty();
    }

    @Override
    public List<StudentDTO> findAllStudents() {
        List<Student> students = studentRepository.findAll();
        List<StudentDTO> dtosList = new ArrayList<>();

        for (Student student : students) {
            StudentDTO simpleDto = this.convertEntityToDto(student);
            dtosList.add(simpleDto);
        }
        return dtosList;
    }

    @Override
    public String deleteById(Long id) {

        Student student = studentRepository.findById(id).orElse(null);

        if (student != null) {

            student = this.unlinkUserToStudent(student, student.getUser().getId());

            studentRepository.deleteById(id);

            return "The student was deleted successfully";
        }
        return "The student was not found";
    }

    private Student unlinkUserToStudent(Student student, Long id_user) {

        userService.unlinkStudent(id_user);

        student.setUser(null);

        return student;
    }

    @Override
    public Optional<StudentDTO> updateStudent(StudentDTO studentDTO) {

        Student student = studentRepository.findById( studentDTO.id_student() ).orElse(null);

        if ( student != null) {

            student.setFirstName(studentDTO.firstName());
            student.setLastName(studentDTO.lastName());
            student.setDate_of_birth(studentDTO.date_of_birth());
            student.setCellphone(studentDTO.cellphone());
            student.setMajor(studentDTO.major());
            student.setGender(studentDTO.gender());
            student.setEmail(studentDTO.email());
            student.setDni(studentDTO.dni());

            studentRepository.save(student);

            StudentDTO newDto = this.convertEntityToDto(student);

            return Optional.of(newDto);
        }
        return Optional.empty();
    }

    @Override
    public List<Student> findStudentsByIds(List<Long> ids_students) {
        return studentRepository.findByIdIn(ids_students);
    }
}
