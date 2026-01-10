package com.security.notasescolares.service;

import com.security.notasescolares.model.Alumn;
import com.security.notasescolares.model.Subject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectService implements ISubjectService {

    @Override
    public List<Subject> getCalifications(Long alumn_id) {

        Alumn alumn = this.createNewAlumn(alumn_id);
        return this.createSubjects(alumn_id);
    }

    private List<Subject> createSubjects(Long alumn_id) {
        List<Subject> subjects = new ArrayList<>();

        subjects.add(new Subject(1L, "Mathematics", 8.5, alumn_id));
        subjects.add(new Subject(2L, "Physics", 7.2, alumn_id));
        subjects.add(new Subject(3L, "Chemistry", 9.1, alumn_id));
        subjects.add(new Subject(4L, "Biology", 8.8, alumn_id));
        subjects.add(new Subject(5L, "History", 7.9, alumn_id));
        subjects.add(new Subject(6L, "Geography", 8.3, alumn_id));
        subjects.add(new Subject(7L, "Literature", 9.0, alumn_id));
        subjects.add(new Subject(8L, "Computer Science", 9.7, alumn_id));

        return subjects;
    }

    private Alumn createNewAlumn(Long alumnId) {
        Alumn  alumn = new Alumn();
        alumn.setId(alumnId);
        alumn.setName("matias");
        alumn.setSurname("rodriguez");
        return alumn;
    }

    @Override
    public double getAverage(Long alumn_id) {

        int counter = 0;

        double result = 0;

        double acumulator = 0;

        List<Subject> subjects =  this.createSubjects(alumn_id);

        for( Subject subject : subjects){

           counter++;

           acumulator +=  subject.getCalification();
        }
        result = acumulator / counter;
        return result;
    }

}
