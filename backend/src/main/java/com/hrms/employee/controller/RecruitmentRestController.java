package com.hrms.employee.controller;

import com.hrms.employee.model.Interview;
import com.hrms.employee.model.JobRequisition;
import com.hrms.employee.model.Offer;
import com.hrms.employee.model.Recruiter;
import com.hrms.employee.service.InterviewService;
import com.hrms.employee.service.JobRequisitionService;
import com.hrms.employee.service.OfferService;
import com.hrms.employee.service.RecruiterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/recruitment")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class RecruitmentRestController {

    @Autowired
    private RecruiterService candidateService;

    @Autowired
    private JobRequisitionService jobRequisitionService;

    @Autowired
    private InterviewService interviewService;

    @Autowired
    private OfferService offerService;

    // ============ Candidate API ============
    @PostMapping("/candidates")
    public ResponseEntity<?> addCandidate(@Valid @RequestBody Recruiter candidate) {
        try {
            Recruiter saved = candidateService.saveData(candidate);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ============ Job Requisition API ============
    @PostMapping("/job-requisitions")
    public ResponseEntity<?> createJobRequisition(@Valid @RequestBody JobRequisition jobRequisition) {
        try {
            JobRequisition saved = jobRequisitionService.createJobRequisition(jobRequisition);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ============ Interview API ============
    @PostMapping("/interviews")
    public ResponseEntity<?> scheduleInterview(@Valid @RequestBody Interview interview) {
        try {
            Interview scheduled = interviewService.scheduleInterview(interview);
            return ResponseEntity.status(HttpStatus.CREATED).body(scheduled);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ============ Offer API ============
    @PostMapping("/offers")
    public ResponseEntity<?> rolloutOffer(@Valid @RequestBody Offer offer) {
        try {
            Offer saved = offerService.rolloutOffer(offer);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
