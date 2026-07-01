package com.hrms.recruiter.service;

import com.hrms.recruiter.dto.HiredCandidateDTO;
import com.hrms.recruiter.model.Interview;
import com.hrms.recruiter.model.Offer;
import com.hrms.recruiter.model.Recruiter;
import com.hrms.recruiter.repository.InterviewRepository;
import com.hrms.recruiter.repository.OfferRepository;
import com.hrms.recruiter.repository.RecruiterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private RecruiterRepository recruiterRepository;

    @Mock
    private InterviewRepository interviewRepository;

    @InjectMocks
    private OfferService offerService;

    private Recruiter candidate;
    private Offer offer;
    private Interview completedInterview;

    @BeforeEach
    void setUp() {
        candidate = new Recruiter();
        candidate.setCandidateId(1);
        candidate.setFullName("John Doe");
        candidate.setAppliedRole("Java Developer");
        candidate.setExperienceYears(5);
        candidate.setInterviewStage("Technical");
        candidate.setCandidateStatus(Recruiter.CandidateStatus.IN_INTERVIEW);

        offer = new Offer();
        offer.setOfferId(1);
        offer.setCandidateId(1);
        offer.setPositionOffered("Senior Java Developer");
        offer.setDepartment("Engineering");
        offer.setSalaryOffered(new BigDecimal("1200000.00"));
        offer.setOfferDate(LocalDate.now());
        offer.setJoiningDate(LocalDate.now().plusDays(15));
        offer.setOfferStatus(Offer.OfferStatus.DRAFTED);
        offer.setAdditionalBenefits("Health Insurance");
        offer.setRemarks("Excellent candidate");

        completedInterview = new Interview();
        completedInterview.setInterviewId(1);
        completedInterview.setCandidateId(1);
        completedInterview.setInterviewStatus(Interview.InterviewStatus.COMPLETED);
    }

    // ========== rolloutOffer ==========

    @Test
    void rolloutOfferSuccessfullyWhenCandidateHasCompletedInterview() {
        when(recruiterRepository.findById(1)).thenReturn(Optional.of(candidate));
        when(interviewRepository.findByCandidateId(1)).thenReturn(List.of(completedInterview));
        when(recruiterRepository.save(any(Recruiter.class))).thenReturn(candidate);
        when(offerRepository.save(any(Offer.class))).thenReturn(offer);

        Offer result = offerService.rolloutOffer(offer);

        assertNotNull(result);
        assertEquals(1, result.getOfferId());
        verify(recruiterRepository).save(any(Recruiter.class));
        verify(offerRepository).save(offer);
    }

    @Test
    void rolloutOfferUpdatesCandidateStatusToOffered() {
        when(recruiterRepository.findById(1)).thenReturn(Optional.of(candidate));
        when(interviewRepository.findByCandidateId(1)).thenReturn(List.of(completedInterview));
        when(recruiterRepository.save(any(Recruiter.class))).thenAnswer(i -> i.getArgument(0));
        when(offerRepository.save(any(Offer.class))).thenReturn(offer);

        offerService.rolloutOffer(offer);

        assertEquals(Recruiter.CandidateStatus.OFFERED, candidate.getCandidateStatus());
        assertEquals("Offer Rolled Out", candidate.getInterviewStage());
    }

    @Test
    void rolloutOfferThrowsExceptionWhenCandidateNotFound() {
        when(recruiterRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> offerService.rolloutOffer(offer));
        assertTrue(ex.getMessage().contains("Candidate not found"));
    }

    @Test
    void rolloutOfferThrowsExceptionWhenNoCompletedInterview() {
        Interview scheduledInterview = new Interview();
        scheduledInterview.setInterviewStatus(Interview.InterviewStatus.SCHEDULED);

        when(recruiterRepository.findById(1)).thenReturn(Optional.of(candidate));
        when(interviewRepository.findByCandidateId(1)).thenReturn(List.of(scheduledInterview));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> offerService.rolloutOffer(offer));
        assertTrue(ex.getMessage().contains("has not completed any interview"));
    }

    @Test
    void rolloutOfferThrowsExceptionWhenNoInterviewsExist() {
        when(recruiterRepository.findById(1)).thenReturn(Optional.of(candidate));
        when(interviewRepository.findByCandidateId(1)).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> offerService.rolloutOffer(offer));
    }

    // ========== updateOfferStatus ==========

    @Test
    void updateOfferStatusToAcceptedUpdatesCandidateToHired() {
        offer.setOfferStatus(Offer.OfferStatus.SENT);
        when(offerRepository.findById(1)).thenReturn(Optional.of(offer));
        when(recruiterRepository.findById(1)).thenReturn(Optional.of(candidate));
        when(recruiterRepository.save(any(Recruiter.class))).thenReturn(candidate);
        when(offerRepository.save(any(Offer.class))).thenAnswer(i -> i.getArgument(0));

        Offer result = offerService.updateOfferStatus(1, Offer.OfferStatus.ACCEPTED);

        assertEquals(Offer.OfferStatus.ACCEPTED, result.getOfferStatus());
        assertEquals(Recruiter.CandidateStatus.HIRED, candidate.getCandidateStatus());
        assertEquals("Hired", candidate.getInterviewStage());
    }

    @Test
    void updateOfferStatusToRejectedDoesNotChangeCandidateStatus() {
        offer.setOfferStatus(Offer.OfferStatus.SENT);
        when(offerRepository.findById(1)).thenReturn(Optional.of(offer));
        when(offerRepository.save(any(Offer.class))).thenAnswer(i -> i.getArgument(0));

        Offer result = offerService.updateOfferStatus(1, Offer.OfferStatus.REJECTED);

        assertEquals(Offer.OfferStatus.REJECTED, result.getOfferStatus());
        verify(recruiterRepository, never()).save(any(Recruiter.class));
    }

    @Test
    void updateOfferStatusThrowsExceptionWhenOfferNotFound() {
        when(offerRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> offerService.updateOfferStatus(99, Offer.OfferStatus.ACCEPTED));
        assertTrue(ex.getMessage().contains("Offer not found"));
    }

    // ========== getHiredCandidateDTO ==========

    @Test
    void getHiredCandidateDTOReturnsCorrectDataForAcceptedOffer() {
        offer.setOfferStatus(Offer.OfferStatus.ACCEPTED);
        when(offerRepository.findById(1)).thenReturn(Optional.of(offer));
        when(recruiterRepository.findById(1)).thenReturn(Optional.of(candidate));

        HiredCandidateDTO dto = offerService.getHiredCandidateDTO(1);

        assertEquals(1, dto.getCandidateId());
        assertEquals("John Doe", dto.getFullName());
        assertEquals("Java Developer", dto.getAppliedRole());
        assertEquals(5, dto.getExperienceYears());
        assertEquals(1, dto.getOfferId());
        assertEquals("Senior Java Developer", dto.getPositionOffered());
        assertEquals("Engineering", dto.getDepartment());
        assertEquals(new BigDecimal("1200000.00"), dto.getSalaryOffered());
        assertEquals("Health Insurance", dto.getAdditionalBenefits());
    }

    @Test
    void getHiredCandidateDTOThrowsExceptionWhenOfferNotAccepted() {
        offer.setOfferStatus(Offer.OfferStatus.SENT);
        when(offerRepository.findById(1)).thenReturn(Optional.of(offer));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> offerService.getHiredCandidateDTO(1));
        assertTrue(ex.getMessage().contains("not in ACCEPTED status"));
    }

    @Test
    void getHiredCandidateDTOThrowsExceptionWhenOfferNotFound() {
        when(offerRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> offerService.getHiredCandidateDTO(99));
    }

    @Test
    void getHiredCandidateDTOThrowsExceptionWhenCandidateNotFound() {
        offer.setOfferStatus(Offer.OfferStatus.ACCEPTED);
        when(offerRepository.findById(1)).thenReturn(Optional.of(offer));
        when(recruiterRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> offerService.getHiredCandidateDTO(1));
    }

    // ========== Basic CRUD ==========

    @Test
    void getAllOffersReturnsListFromRepository() {
        when(offerRepository.findAll()).thenReturn(List.of(offer));

        List<Offer> result = offerService.getAllOffers();

        assertEquals(1, result.size());
        assertEquals(offer, result.get(0));
    }

    @Test
    void getOfferByIdReturnsOfferWhenExists() {
        when(offerRepository.findById(1)).thenReturn(Optional.of(offer));

        Offer result = offerService.getOfferById(1);

        assertNotNull(result);
        assertEquals(1, result.getOfferId());
    }

    @Test
    void getOfferByIdReturnsNullWhenNotFound() {
        when(offerRepository.findById(99)).thenReturn(Optional.empty());

        Offer result = offerService.getOfferById(99);

        assertNull(result);
    }

    @Test
    void deleteOfferCallsRepositoryDeleteById() {
        offerService.deleteOffer(1);

        verify(offerRepository).deleteById(1);
    }

    @Test
    void getOfferCountReturnsRepositoryCount() {
        when(offerRepository.count()).thenReturn(5L);

        long count = offerService.getOfferCount();

        assertEquals(5L, count);
    }

    @Test
    void getOffersByCandidateIdReturnsMatchingOffers() {
        when(offerRepository.findByCandidateId(1)).thenReturn(List.of(offer));

        List<Offer> result = offerService.getOffersByCandidateId(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getCandidateId());
    }
}

