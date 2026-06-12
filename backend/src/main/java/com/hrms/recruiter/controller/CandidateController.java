package com.hrms.recruiter.controller;

import com.hrms.recruiter.model.Candidate;
import com.hrms.recruiter.service.CandidateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

    @GetMapping("/candidateForm")
    public String showForm(Model model) {
        model.addAttribute("candidate", new Candidate());
        return "candidateForm";
    }
    @PostMapping("/candidateForm")
    public String saveCandidate(@Valid @ModelAttribute Candidate candidate, 
                                BindingResult bindingResult, 
                                Model model) {
        if (bindingResult.hasErrors()) {
            // Validation errors exist, return to form with error messages
            return "candidateForm";
        }
        candidateService.saveData(candidate);
        model.addAttribute("message", "Candidate saved successfully!");
        model.addAttribute("candidate", new Candidate());
        return "candidateForm";
    }
}
