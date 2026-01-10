package com.security.notasescolares.controller;

import com.security.notasescolares.model.Subject;
import com.security.notasescolares.service.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NoteController {

    @Autowired
    private ISubjectService iSubjectService;

    @GetMapping("/califications/{alumn_id}")
    public List<Subject> getCalifications(@PathVariable Long alumn_id){
        return iSubjectService.getCalifications(alumn_id);
    }

    @GetMapping("/average/{alumn_id}")
    public double getAverage(@PathVariable Long alumn_id){
        return iSubjectService.getAverage(alumn_id);
    }
}
