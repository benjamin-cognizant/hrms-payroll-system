package com.hrms.recruiter.service;

import com.hrms.recruiter.model.Candidate;
import com.hrms.recruiter.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;
    public Candidate saveData(Candidate candidate){
        return candidateRepository.save(candidate);
    }
}
