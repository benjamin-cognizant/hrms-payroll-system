package com.hrms.performance.controller;

import com.hrms.performance.model.AppraisalRecord;
import com.hrms.performance.service.AppraisalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
// Fixed: Explicitly imported the correct Spring Web annotations
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequestMapping("/appraisal")
public class AppraisalController {

    @Autowired
    private AppraisalService appraisalService;

    // Fixed: Properly wired the missing dashboard route
    @GetMapping("/dashboard")
    public String viewDashboard() {
        return "appraisal-dashboard"; // Maps to appraisal-dashboard.html
    }

    @PostMapping("/setGoals")
    public String setGoals(@RequestParam Long employeeId, @RequestParam String appraisalCycle, Model model) {
        AppraisalRecord record = appraisalService.initializeGoals(employeeId, appraisalCycle);
        model.addAttribute("appraisal", record);
        return "appraisal-details"; 
    }

    @PostMapping("/recordSelfReview")
    public String recordSelfReview(@RequestParam Long appraisalId, @RequestParam Integer goalsAchieved, Model model) {
        AppraisalRecord record = appraisalService.submitSelfReview(appraisalId, goalsAchieved);
        model.addAttribute("appraisal", record);
        return "appraisal-details";
    }

    @PostMapping("/submitManagerReview")
    public String submitManagerReview(@RequestParam Long appraisalId, @RequestParam BigDecimal overallRating, Model model) {
        AppraisalRecord record = appraisalService.submitManagerReview(appraisalId, overallRating);
        model.addAttribute("appraisal", record);
        return "appraisal-details";
    }

    @PostMapping("/publishRating")
    public String publishRating(@RequestParam Long appraisalId, Model model) {
        AppraisalRecord record = appraisalService.publishFinalRating(appraisalId);
        model.addAttribute("appraisal", record);
        return "appraisal-details";
    }
}