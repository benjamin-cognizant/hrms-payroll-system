package com.hrms.recruiter.service;

import com.hrms.recruiter.model.Interview;
import com.hrms.recruiter.model.Recruiter;
import com.hrms.recruiter.repository.InterviewRepository;
import com.hrms.recruiter.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InterviewService {
    
    @Autowired
    private InterviewRepository interviewRepository;
    
    @Autowired
    private RecruiterRepository recruiterRepository;
    
    @Transactional
    public Interview scheduleInterview(Interview interview) {
        // Update candidate status to IN_INTERVIEW
        Recruiter candidate = recruiterRepository.findById(interview.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found with ID: " + interview.getCandidateId()));
        
        candidate.setCandidateStatus(Recruiter.CandidateStatus.IN_INTERVIEW);
        candidate.setInterviewStage(interview.getInterviewRound());
        recruiterRepository.save(candidate);
        
        return interviewRepository.save(interview);
    }
    
    public List<Interview> getAllInterviews() {
        return interviewRepository.findAll();
    }
    
    public Interview getInterviewById(Integer id) {
        return interviewRepository.findById(id).orElse(null);
    }
    
    public List<Interview> getInterviewsByCandidateId(Integer candidateId) {
        return interviewRepository.findByCandidateId(candidateId);
    }
    
    @Transactional
    public Interview updateInterview(Interview interview) {
        // Update candidate status based on interview status
        Recruiter candidate = recruiterRepository.findById(interview.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found with ID: " + interview.getCandidateId()));

        switch (interview.getInterviewStatus()) {
            case COMPLETED:
                candidate.setCandidateStatus(Recruiter.CandidateStatus.IN_INTERVIEW);
                candidate.setInterviewStage(interview.getInterviewRound() + " - Completed");
                break;
            case CANCELLED:
            case NO_SHOW:
                candidate.setCandidateStatus(Recruiter.CandidateStatus.REJECTED);
                candidate.setInterviewStage(interview.getInterviewRound() + " - " + interview.getInterviewStatus().name());
                break;
            case SCHEDULED:
            case RESCHEDULED:
                candidate.setCandidateStatus(Recruiter.CandidateStatus.IN_INTERVIEW);
                candidate.setInterviewStage(interview.getInterviewRound());
                break;
        }
        recruiterRepository.save(candidate);

        return interviewRepository.save(interview);
    }
    
    public void deleteInterview(Integer id) {
        interviewRepository.deleteById(id);
    }
    
    public long getInterviewCount() {
        return interviewRepository.count();
    }
}


