package com.hrms.employee.service;

import com.hrms.employee.model.Recruiter;
import com.hrms.employee.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RecruiterService {
    @Autowired
    private RecruiterRepository candidateRepository;
    
    public Recruiter saveData(Recruiter candidate){
        return candidateRepository.save(candidate);
    }
    
    public List<Recruiter> getAllCandidates() {
        return candidateRepository.findAll();
    }
    
    public Recruiter getCandidateById(Integer id) {
        return candidateRepository.findById(id).orElse(null);
    }
    
    public Recruiter updateCandidate(Recruiter candidate) {
        return candidateRepository.save(candidate);
    }
    
    public void deleteCandidate(Integer id) {
        candidateRepository.deleteById(id);
    }
    
    public long getCandidateCount() {
        return candidateRepository.count();
    }
}
