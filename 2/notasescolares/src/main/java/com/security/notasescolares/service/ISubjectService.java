package com.security.notasescolares.service;

import com.security.notasescolares.model.Subject;

import java.util.List;

public interface ISubjectService {

    public List<Subject> getCalifications(Long alumn_id);

    public double getAverage(Long alumn_id);
}
