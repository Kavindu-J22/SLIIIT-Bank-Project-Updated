package com.bankingsystem.repository;

import com.bankingsystem.model.FixDeposit;
import com.bankingsystem.model.FixDepositStatus;
import com.bankingsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FixDepositRepository extends JpaRepository<FixDeposit, Long> {
    List<FixDeposit> findByApplicant(User applicant);
    List<FixDeposit> findByStatus(FixDepositStatus status);
}