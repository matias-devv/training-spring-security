package com.education.platform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String courseName;

    @ManyToMany( fetch = FetchType.EAGER)
    @JoinTable( name = "courses_students", joinColumns = @JoinColumn( name = "course_id"),
                inverseJoinColumns = @JoinColumn (name = "student_id"))
    List<Student> students;

    @ManyToOne(fetch = FetchType.EAGER)
    private Professor professor;
}
