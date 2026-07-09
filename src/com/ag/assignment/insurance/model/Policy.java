package com.ag.assignment.insurance.model;

import javax.management.OperationsException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;

public abstract class Policy {
    private final BigInteger policyNumber;
    private PolicyHolder policyHolder;

    private final Instant createdAt;
    private Instant updatedAt;
    private PolicyStatus policyStatus;

    private LocalDate startDate;
    private LocalDate endDate;



    public Policy(){
        Random rand = new Random();
        int numBits = 16;

        this.policyNumber = new BigInteger(numBits, rand).add(BigInteger.valueOf(1000000));
        this.createdAt = Instant.now();
        this.policyStatus = PolicyStatus.PENDING;

    }

    public PolicyStatus getPolicyStatus() {
        return policyStatus;
    }

    public BigInteger getPolicyNumber() {
        return policyNumber;
    }

    protected void setPolicyHolder(PolicyHolder policyHolder) {
        this.policyHolder = policyHolder;
    }

    public PolicyHolder getPolicyHolder() {
        return policyHolder;
    }

    protected void setPolicyStatus(PolicyStatus policyStatus) {
        this.policyStatus = policyStatus;
    }

    protected void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    protected void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public abstract void activate();

    public abstract void cancel();


    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof Policy policy)) return false;

        return policyNumber.equals(policy.policyNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(policyNumber);
    }



}
