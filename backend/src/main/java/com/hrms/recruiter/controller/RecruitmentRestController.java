package com.hrms.recruiter.controller;

import com.hrms.recruiter.model.Interview;
import com.hrms.recruiter.model.JobRequisition;
import com.hrms.recruiter.model.Offer;
import com.hrms.recruiter.model.Recruiter;
import com.hrms.recruiter.service.InterviewService;
import com.hrms.recruiter.service.JobRequisitionService;
import com.hrms.recruiter.service.OfferService;
import com.hrms.recruiter.service.RecruiterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recruitment")
@CrossOrigin(origins = "*")
public class RecruitmentRestController {

    @Autowired
    private RecruiterService candidateService;

    @Autowired
    private JobRequisitionService jobRequisitionService;

    @Autowired
    private InterviewService interviewService;

    @Autowired
    private OfferService offerService;

    // ============ Health Check ============
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "HRMS Recruitment API is running");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    // ============ Dashboard ============
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("candidateCount", candidateService.getCandidateCount());
        dashboard.put("requisitionCount", jobRequisitionService.getRequisitionCount());
        dashboard.put("interviewCount", interviewService.getInterviewCount());
        dashboard.put("offerCount", offerService.getOfferCount());
        return ResponseEntity.ok(dashboard);
    }

    // ============ Candidate APIs ============
    @PostMapping("/candidates")
    public ResponseEntity<?> addCandidate(@Valid @RequestBody Recruiter candidate) {
        try {
            Recruiter saved = candidateService.saveData(candidate);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/candidates")
    public ResponseEntity<List<Recruiter>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }

    @GetMapping("/candidates/{id}")
    public ResponseEntity<?> getCandidateById(@PathVariable Integer id) {
        Recruiter candidate = candidateService.getCandidateById(id);
        if (candidate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Candidate not found"));
        }
        return ResponseEntity.ok(candidate);
    }

    @PutMapping("/candidates/{id}")
    public ResponseEntity<?> updateCandidate(@PathVariable Integer id, @Valid @RequestBody Recruiter candidate) {
        try {
            Recruiter existing = candidateService.getCandidateById(id);
            if (existing == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Candidate not found"));
            }
            candidate.setCandidateId(id);
            Recruiter updated = candidateService.updateCandidate(candidate);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/candidates/{id}")
    public ResponseEntity<?> deleteCandidate(@PathVariable Integer id) {
        Recruiter candidate = candidateService.getCandidateById(id);
        if (candidate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Candidate not found"));
        }
        candidateService.deleteCandidate(id);
        return ResponseEntity.ok(Map.of("message", "Candidate deleted successfully"));
    }

    // ============ Job Requisition APIs ============
    @PostMapping("/job-requisitions")
    public ResponseEntity<?> createJobRequisition(@Valid @RequestBody JobRequisition jobRequisition) {
        try {
            JobRequisition saved = jobRequisitionService.createJobRequisition(jobRequisition);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/job-requisitions")
    public ResponseEntity<List<JobRequisition>> getAllJobRequisitions() {
        return ResponseEntity.ok(jobRequisitionService.getAllJobRequisitions());
    }

    @GetMapping("/job-requisitions/{id}")
    public ResponseEntity<?> getJobRequisitionById(@PathVariable Integer id) {
        JobRequisition req = jobRequisitionService.getJobRequisitionById(id);
        if (req == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Job Requisition not found"));
        }
        return ResponseEntity.ok(req);
    }

    @PutMapping("/job-requisitions/{id}")
    public ResponseEntity<?> updateJobRequisition(@PathVariable Integer id, @Valid @RequestBody JobRequisition jobRequisition) {
        try {
            JobRequisition existing = jobRequisitionService.getJobRequisitionById(id);
            if (existing == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Job Requisition not found"));
            }
            jobRequisition.setRequisitionId(id);
            JobRequisition updated = jobRequisitionService.updateJobRequisition(jobRequisition);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/job-requisitions/{id}")
    public ResponseEntity<?> deleteJobRequisition(@PathVariable Integer id) {
        JobRequisition req = jobRequisitionService.getJobRequisitionById(id);
        if (req == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Job Requisition not found"));
        }
        jobRequisitionService.deleteJobRequisition(id);
        return ResponseEntity.ok(Map.of("message", "Job Requisition deleted successfully"));
    }

    // ============ Interview APIs (scheduleInterview) ============
    @PostMapping("/interviews")
    public ResponseEntity<?> scheduleInterview(@Valid @RequestBody Interview interview) {
        try {
            Interview scheduled = interviewService.scheduleInterview(interview);
            return ResponseEntity.status(HttpStatus.CREATED).body(scheduled);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/interviews")
    public ResponseEntity<List<Interview>> getAllInterviews() {
        return ResponseEntity.ok(interviewService.getAllInterviews());
    }

    @GetMapping("/interviews/{id}")
    public ResponseEntity<?> getInterviewById(@PathVariable Integer id) {
        Interview interview = interviewService.getInterviewById(id);
        if (interview == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Interview not found"));
        }
        return ResponseEntity.ok(interview);
    }

    @GetMapping("/interviews/candidate/{candidateId}")
    public ResponseEntity<List<Interview>> getInterviewsByCandidateId(@PathVariable Integer candidateId) {
        return ResponseEntity.ok(interviewService.getInterviewsByCandidateId(candidateId));
    }

    @PutMapping("/interviews/{id}")
    public ResponseEntity<?> updateInterview(@PathVariable Integer id, @Valid @RequestBody Interview interview) {
        try {
            Interview existing = interviewService.getInterviewById(id);
            if (existing == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Interview not found"));
            }
            interview.setInterviewId(id);
            Interview updated = interviewService.updateInterview(interview);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/interviews/{id}")
    public ResponseEntity<?> deleteInterview(@PathVariable Integer id) {
        Interview interview = interviewService.getInterviewById(id);
        if (interview == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Interview not found"));
        }
        interviewService.deleteInterview(id);
        return ResponseEntity.ok(Map.of("message", "Interview deleted successfully"));
    }

    // ============ Offer APIs (rolloutOffer) ============
    @PostMapping("/offers")
    public ResponseEntity<?> rolloutOffer(@Valid @RequestBody Offer offer) {
        try {
            Offer saved = offerService.rolloutOffer(offer);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/offers")
    public ResponseEntity<List<Offer>> getAllOffers() {
        return ResponseEntity.ok(offerService.getAllOffers());
    }

    @GetMapping("/offers/{id}")
    public ResponseEntity<?> getOfferById(@PathVariable Integer id) {
        Offer offer = offerService.getOfferById(id);
        if (offer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Offer not found"));
        }
        return ResponseEntity.ok(offer);
    }

    @GetMapping("/offers/candidate/{candidateId}")
    public ResponseEntity<List<Offer>> getOffersByCandidateId(@PathVariable Integer candidateId) {
        return ResponseEntity.ok(offerService.getOffersByCandidateId(candidateId));
    }

    @PutMapping("/offers/{id}")
    public ResponseEntity<?> updateOffer(@PathVariable Integer id, @Valid @RequestBody Offer offer) {
        try {
            Offer existing = offerService.getOfferById(id);
            if (existing == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Offer not found"));
            }
            offer.setOfferId(id);
            Offer updated = offerService.updateOffer(offer);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/offers/{id}/status")
    public ResponseEntity<?> updateOfferStatus(@PathVariable Integer id, @RequestBody Map<String, String> statusUpdate) {
        try {
            String statusStr = statusUpdate.get("status");
            Offer.OfferStatus newStatus = Offer.OfferStatus.valueOf(statusStr);
            Offer updated = offerService.updateOfferStatus(id, newStatus);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid status value"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/offers/{id}")
    public ResponseEntity<?> deleteOffer(@PathVariable Integer id) {
        Offer offer = offerService.getOfferById(id);
        if (offer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Offer not found"));
        }
        offerService.deleteOffer(id);
        return ResponseEntity.ok(Map.of("message", "Offer deleted successfully"));
    }
}

