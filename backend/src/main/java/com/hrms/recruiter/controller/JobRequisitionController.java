package com.hrms.recruiter.controller;

import com.hrms.recruiter.model.JobRequisition;
import com.hrms.recruiter.service.JobRequisitionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/jobRequisition")
public class JobRequisitionController {
    
    @Autowired
    private JobRequisitionService jobRequisitionService;

    /**
     * Display the job requisition form
     */
    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("jobRequisition", new JobRequisition());
        return "jobRequisitionForm";
    }
    @PostMapping("/create")
    public String createJobRequisition(@Valid @ModelAttribute JobRequisition jobRequisition,
                                       BindingResult bindingResult,
                                       Model model) {
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            return "jobRequisitionForm";
        }

        // Save the job requisition
        jobRequisitionService.createJobRequisition(jobRequisition);
        model.addAttribute("message", "Job Requisition created successfully!");
        model.addAttribute("jobRequisition", new JobRequisition());
        return "jobRequisitionForm";
    }

    /**
     * Display all job requisitions
     */
    @GetMapping("/list")
    public String listJobRequisitions(Model model) {
        model.addAttribute("requisitions", jobRequisitionService.getAllJobRequisitions());
        return "jobRequisitionList";
    }
}

