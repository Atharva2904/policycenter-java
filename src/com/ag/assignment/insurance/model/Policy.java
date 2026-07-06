package com.ag.assignment.insurance.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;

public abstract class Policy {
    private final BigInteger policyNumber;
    private final Instant createdAt;
    private Instant updatedAt;
    private PolicyStatus policyStatus;
    private int policyLimit;
    private int perClaimCost;

    private PolicyHolder policyHolder;
    private LocalDate startDate;
    private LocalDate endDate;


    private BigDecimal basePremium;

    public Policy(){
        Random rand = new Random();
        int numBits = 16;

        this.policyNumber = new BigInteger(numBits, rand).add(BigInteger.valueOf(1000000));
        this.createdAt = Instant.now();
        this.policyStatus = PolicyStatus.PENDING;

        this.policyLimit = 1;
        this.perClaimCost = 500;
    }

    public Policy(BigInteger policyNumber){
        this.policyNumber = policyNumber;
        this.createdAt = Instant.now();
        this.policyStatus = PolicyStatus.PENDING;

        this.policyLimit = 1;
    }

    public PolicyStatus getPolicyStatus() {
        return policyStatus;
    }

    public BigInteger getPolicyNumber() {
        return policyNumber;
    }


    public int getPolicyLimit() {
        return policyLimit;
    }

    protected void setPolicyHolder(PolicyHolder policyHolder) {
        this.policyHolder = policyHolder;
    }

    protected void setBasePremium(BigDecimal basePremium) {
        this.basePremium = basePremium;
    }

    protected void setPolicyLimit(int newPolicyLimit){
        this.policyLimit = newPolicyLimit;
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

    public int getPerClaimCost() {
        return perClaimCost;
    }

    public BigDecimal getBasePremium() {
        return basePremium;
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
