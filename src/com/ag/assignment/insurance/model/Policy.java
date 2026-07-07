package com.ag.assignment.insurance.model;

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

    public Policy(BigInteger policyNumber){
        this.policyNumber = policyNumber;
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

    public void activate(){
        this.policyStatus = PolicyStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void cancel(){
        this.policyStatus = PolicyStatus.CANCELLED;
        this.updatedAt = Instant.now();
    }


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
