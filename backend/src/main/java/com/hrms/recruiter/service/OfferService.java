package com.hrms.recruiter.service;

import com.hrms.recruiter.model.Offer;
import com.hrms.recruiter.model.Recruiter;
import com.hrms.recruiter.repository.OfferRepository;
import com.hrms.recruiter.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Transactional
    public Offer rolloutOffer(Offer offer) {
        // Update candidate status to OFFERED
        Recruiter candidate = recruiterRepository.findById(offer.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found with ID: " + offer.getCandidateId()));

        candidate.setCandidateStatus(Recruiter.CandidateStatus.OFFERED);
        candidate.setInterviewStage("Offer Rolled Out");
        recruiterRepository.save(candidate);

        return offerRepository.save(offer);
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public Offer getOfferById(Integer id) {
        return offerRepository.findById(id).orElse(null);
    }

    public List<Offer> getOffersByCandidateId(Integer candidateId) {
        return offerRepository.findByCandidateId(candidateId);
    }

    public Offer updateOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    public void deleteOffer(Integer id) {
        offerRepository.deleteById(id);
    }

    public long getOfferCount() {
        return offerRepository.count();
    }

    @Transactional
    public Offer updateOfferStatus(Integer offerId, Offer.OfferStatus newStatus) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found with ID: " + offerId));

        offer.setOfferStatus(newStatus);

        // If offer is accepted, update candidate status to HIRED
        if (newStatus == Offer.OfferStatus.ACCEPTED) {
            Recruiter candidate = recruiterRepository.findById(offer.getCandidateId())
                    .orElseThrow(() -> new RuntimeException("Candidate not found"));
            candidate.setCandidateStatus(Recruiter.CandidateStatus.HIRED);
            candidate.setInterviewStage("Hired");
            recruiterRepository.save(candidate);
        }

        return offerRepository.save(offer);
    }
}

