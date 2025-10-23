package com.bankingsystem.service;

import com.bankingsystem.model.FixDeposit;
import com.bankingsystem.model.FixDepositStatus;
import com.bankingsystem.model.User;
import com.bankingsystem.repository.FixDepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FixDepositService {

    @Autowired
    private FixDepositRepository fixDepositRepository;

    public FixDeposit applyFixDeposit(FixDeposit fixDeposit, User applicant) {
        fixDeposit.setApplicant(applicant);
        fixDeposit.setStatus(FixDepositStatus.PENDING);
        return fixDepositRepository.save(fixDeposit);
    }

    public void reviewFixDeposit(Long id, User reviewer, FixDepositStatus status) {
        FixDeposit fixDeposit = fixDepositRepository.findById(id).orElseThrow(() -> new RuntimeException("Fix Deposit not found"));
        fixDeposit.setReviewer(reviewer);
        fixDeposit.setStatus(status);
        fixDepositRepository.save(fixDeposit);
    }

    public void updateStatus(Long id, User approver, FixDepositStatus status) {
        FixDeposit fixDeposit = fixDepositRepository.findById(id).orElseThrow(() -> new RuntimeException("Fix Deposit not found"));
        fixDeposit.setApprover(approver);
        fixDeposit.setStatus(status);
        fixDepositRepository.save(fixDeposit);
    }

    public List<FixDeposit> findAll() {
        return fixDepositRepository.findAll();
    }

    public Optional<FixDeposit> findById(Long id) {
        return fixDepositRepository.findById(id);
    }

    public List<FixDeposit> findByApplicant(User applicant) {
        return fixDepositRepository.findByApplicant(applicant);
    }

    public void deleteById(Long id) {
        fixDepositRepository.deleteById(id);
    }
}