package com.ag.assignment.insurance.model;

import com.ag.assignment.insurance.service.PolicyValidator;

import javax.management.OperationsException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;

public class Policy {
    private final String policyNumber;
    private PolicyHolder policyHolder;

    private final Instant createdAt;
    private Instant updatedAt;
    private PolicyStatus policyStatus;

    private LocalDate startDate;
    private LocalDate endDate;

    private VehicleType vehicleType;
    private int previousClaimsCount;

    public Policy(String policyNumber, PolicyHolder policyHolder, VehicleType vehicleType, int previousClaimsCount) {
        this.policyNumber = policyNumber;
        this.policyHolder = policyHolder;
        this.vehicleType = vehicleType;
        this.previousClaimsCount = previousClaimsCount;
        this.createdAt = Instant.now();
        this.startDate = LocalDate.now();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public int getPreviousClaimsCount() {
        return previousClaimsCount;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public PolicyStatus getPolicyStatus() {
        return policyStatus;
    }

    public PolicyHolder getPolicyHolder() {
        return policyHolder;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Policy policy)) return false;

        return policyNumber.equals(policy.policyNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(policyNumber);
    }


}
