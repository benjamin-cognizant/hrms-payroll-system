package org.example.controller;


import org.example.model.Student;
import org.example.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/s")
    public String showStudent(Model model) {
        model.addAttribute("student", new Student());
        return "a";
    }

    @PostMapping("/student")
    public String saveStudent(@Valid Student student, BindingResult result) {
        if (result.hasErrors()) {
            return "a";
        } else {
            return "b";
        }
    }

    @PostMapping("/login")
    public String login(@Valid Student user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "a";
        }
        return "c";
    }
}

