package com.bankingsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FixDeposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Fix Deposit type is required")
    private String fixDepositType;

    @Positive(message = "Amount must be positive")
    private double amount;

    @Positive(message = "Duration must be positive")
    private int duration;

    @Enumerated(EnumType.STRING)
    private FixDepositStatus status;

    @ManyToOne
    private User applicant;

    @ManyToOne
    private User reviewer;

    @ManyToOne
    private User approver;
}