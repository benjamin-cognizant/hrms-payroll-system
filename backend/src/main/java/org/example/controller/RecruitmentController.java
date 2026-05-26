package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecruitmentController {
    @GetMapping("createJobRequisition")
    public String createJobRequisition(){
        return "Job created";
    }
    @GetMapping("addCandidate")
    public String addCandidate(){
        return "Candidate added";
    }
    @GetMapping("scheduleInterview")
    public String scheduleInterview(){
        return "Interview scheduled";
    }
    @GetMapping("rolloutOffer")
    public String rolloutOffer(){
        return "Offer rolled out";
    }
}
