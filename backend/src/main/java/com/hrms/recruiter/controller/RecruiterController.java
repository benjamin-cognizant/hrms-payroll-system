package com.hrms.recruiter.controller;

import com.hrms.recruiter.model.Recruiter;
import com.hrms.recruiter.model.JobRequisition;
import com.hrms.recruiter.service.RecruiterService;
import com.hrms.recruiter.service.JobRequisitionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RecruiterController {
    @Autowired
    private RecruiterService candidateService;

    @Autowired
    private JobRequisitionService jobRequisitionService;

    @GetMapping("/recruiter/dashboard")
    public String recruiterDashboard(Model model) {
        model.addAttribute("candidateCount", candidateService.getCandidateCount());
        model.addAttribute("requisitionCount", jobRequisitionService.getRequisitionCount());
        return "recruiterDashboard";
    }

    @GetMapping("/candidateForm")
    public String addCandidate(Model model) {
        model.addAttribute("candidate", new Recruiter());
        return "candidateForm";
    }

    @PostMapping("/candidateForm")
    public String saveCandidate(@Valid @ModelAttribute("candidate") Recruiter candidate,
                                BindingResult bindingResult, 
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("candidate", candidate);
            return "candidateForm";
        }
        
        boolean isUpdate = (candidate.getCandidateId() != null);
        candidateService.saveData(candidate);
        if (isUpdate) {
            return "redirect:/candidates/list?message=Candidate updated successfully!";
        } else {
            model.addAttribute("message", "Candidate saved successfully!");
            model.addAttribute("candidate", new Recruiter());
            return "candidateForm";
        }
    }

    @GetMapping("/candidates/list")
    public String listCandidates(@RequestParam(required = false) String message, Model model) {
        if (message != null && !message.isEmpty()) {
            model.addAttribute("message", message);
        }
        model.addAttribute("candidates", candidateService.getAllCandidates());
        return "candidateList";
    }

    @GetMapping("/candidates/edit/{id}")
    public String editCandidate(@PathVariable Integer id, Model model) {
        Recruiter candidate = candidateService.getCandidateById(id);
        if (candidate == null) {
            return "redirect:/candidates/list?message=Candidate not found!";
        }
        model.addAttribute("candidate", candidate);
        return "candidateForm";
    }

    @GetMapping("/candidates/delete/{id}")
    public String deleteCandidate(@PathVariable Integer id) {
        Recruiter candidate = candidateService.getCandidateById(id);
        if (candidate != null) {
            String name = candidate.getFullName();
            candidateService.deleteCandidate(id);
            return "redirect:/candidates/list?message=Candidate '" + name + "' deleted successfully!";
        }
        return "redirect:/candidates/list?message=Candidate not found!";
    }

    @GetMapping("/jobRequisition/form")
    public String jobRequisition(Model model) {
        model.addAttribute("jobRequisition", new JobRequisition());
        return "jobRequisitionForm";
    }

    @PostMapping("/jobRequisition/create")
    public String createJobRequisition(@Valid @ModelAttribute("jobRequisition") JobRequisition jobRequisition,
                                       BindingResult bindingResult,
                                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("jobRequisition", jobRequisition);
            return "jobRequisitionForm";
        }
        boolean isUpdate = (jobRequisition.getRequisitionId() != null);
        jobRequisitionService.createJobRequisition(jobRequisition);
        
        if (isUpdate) {
            return "redirect:/jobRequisition/list?message=Job Requisition updated successfully!";
        } else {
            model.addAttribute("message", "Job Requisition created successfully!");
            model.addAttribute("jobRequisition", new JobRequisition());
            return "jobRequisitionForm";
        }
    }

    @GetMapping("/jobRequisition/list")
    public String listJobRequisitions(@RequestParam(required = false) String message, Model model) {
        if (message != null && !message.isEmpty()) {
            model.addAttribute("message", message);
        }
        model.addAttribute("requisitions", jobRequisitionService.getAllJobRequisitions());
        return "jobRequisitionList";
    }

    @GetMapping("/jobRequisition/edit/{id}")
    public String editJobRequisition(@PathVariable Integer id, Model model) {
        JobRequisition jobRequisition = jobRequisitionService.getJobRequisitionById(id);
        if (jobRequisition == null) {
            return "redirect:/jobRequisition/list?message=Job Requisition not found!";
        }
        model.addAttribute("jobRequisition", jobRequisition);
        return "jobRequisitionForm";
    }

    @GetMapping("/jobRequisition/delete/{id}")
    public String deleteJobRequisition(@PathVariable Integer id) {
        JobRequisition jobRequisition = jobRequisitionService.getJobRequisitionById(id);
        if (jobRequisition != null) {
            String title = jobRequisition.getJobTitle();
            jobRequisitionService.deleteJobRequisition(id);
            return "redirect:/jobRequisition/list?message=Job Requisition '" + title + "' deleted successfully!";
        }
        return "redirect:/jobRequisition/list?message=Job Requisition not found!";
    }
}
