package org.example.service;

import org.example.model.Student;
import org.example.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;

public class StudentService {
    @Autowired
    private StudentRepo studentRepo;
    public Student findStudentById(Integer id){
        return studentRepo.findById(id).get();
    }
    public void saveStudent(Student student){
        studentRepo.save(student);
    }
}
