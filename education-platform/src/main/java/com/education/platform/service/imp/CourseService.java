package com.education.platform.service.imp;

import com.education.platform.dto.CourseDTO;
import com.education.platform.dto.ProfessorDTO;
import com.education.platform.model.Course;
import com.education.platform.model.Professor;
import com.education.platform.model.Student;
import com.education.platform.repository.ICourseRepository;
import com.education.platform.service.interfaces.ICourseService;
import com.education.platform.service.interfaces.IProfessorService;
import com.education.platform.service.interfaces.IStudentService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService implements ICourseService {

    private final ICourseRepository courseRepository;

    private final IStudentService studentService;

    private final IProfessorService professorService;

    public CourseService(ICourseRepository courseRepository, IStudentService studentService,
                         IProfessorService professorService) {
        this.courseRepository = courseRepository;
        this.studentService = studentService;
        this.professorService = professorService;
    }

    @Override
    public Optional<CourseDTO> createCourse(CourseDTO courseDTO) {

        Course course = new Course();

        List<Student> students = this.findStudents( courseDTO.ids_students());

        Professor professorEntity = this.verifyIfProfessorExist( courseDTO.id_professor() );

        course = this.asignStudentsToCourse( students, course);

        course = this.assignProfessorToCourse( professorEntity, course);

        course.setCourseName( courseDTO.courseName() );

        courseRepository.save(course);

        CourseDTO dto = this.convertEntityToDto(course);

        return Optional.of(dto);
    }

    private Course assignProfessorToCourse(Professor professorEntity, Course course) {

        course.setProfessor(professorEntity);

        professorService.linkCourseToProfessor(course, professorEntity.getId());

        return course;
    }

    private Course asignStudentsToCourse(List<Student> students, Course course) {
        course.setStudents(students);
        return course;
    }

    private Professor verifyIfProfessorExist(Long id_professor) {
        return professorService.findEntityById( id_professor );
    }


    private List<Student> findStudents( List<Long> id_students ) {

        List<Student> students = studentService.findStudentsByIds( id_students );
        if ( students != null && !students.isEmpty() ){
            return students;
        }
        return null;
    }


    private CourseDTO convertEntityToDto(Course course) {

        List<Long> ids_students = new ArrayList<>();

        course.getStudents().forEach(student -> ids_students.add( student.getId() ) );

        return new CourseDTO( course.getId(),
                              course.getCourseName(),
                              ids_students,
                              course.getProfessor().getId() );
    }

    @Override
    public Optional<CourseDTO> findById(Long id) {

        Optional<Course> course = courseRepository.findById(id);

        return course.map(this::convertEntityToDto);
    }

    @Override
    public List<CourseDTO> findAllCourses() {

        List<Course> courseList = courseRepository.findAll();
        return courseList.stream()
                         .map(this::convertEntityToDto)
                         .toList();
    }

    @Override
    public String deleteById(Long id) {
        courseRepository.deleteById(id);
        return "The course was eliminated successfully";
    }

    @Override
    public Optional<CourseDTO> updateCourse(CourseDTO courseDTO) {

        Course courseRead = courseRepository.findById(courseDTO.id()) .orElse(null);

        Professor professor = this.verifyIfProfessorExist(courseDTO.id_professor());

        if( courseRead != null ) {

            if ( courseRead.getProfessor().getId().equals( professor.getId() ) ) {

                courseRead.setCourseName( courseDTO.courseName() );
                courseRead.setStudents( studentService.findStudentsByIds( courseDTO.ids_students() ) );

                courseRepository.save(courseRead);

                CourseDTO dto = this.convertEntityToDto(courseRead);

                return Optional.of(dto);
            }
            throw new RuntimeException("The professor must own the course to edit it");
        }
        return Optional.empty();
    }
}
