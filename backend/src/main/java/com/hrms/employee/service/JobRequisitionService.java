package com.hrms.employee.service;
import com.hrms.employee.model.JobRequisition;
import com.hrms.employee.repository.JobRequisitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class JobRequisitionService {
    @Autowired
    private JobRequisitionRepository jobRequisitionRepository;
    
    public JobRequisition createJobRequisition(JobRequisition jobRequisition) {
        return jobRequisitionRepository.save(jobRequisition);
    }
    public List<JobRequisition> getAllJobRequisitions() {
        return jobRequisitionRepository.findAll();
    }
    public JobRequisition getJobRequisitionById(Integer id) {
        return jobRequisitionRepository.findById(id).orElse(null);
    }
    public JobRequisition updateJobRequisition(JobRequisition jobRequisition) {
        return jobRequisitionRepository.save(jobRequisition);
    }
    public void deleteJobRequisition(Integer id) {
        jobRequisitionRepository.deleteById(id);
    }
    
    public long getRequisitionCount() {
        return jobRequisitionRepository.count();
    }
}

