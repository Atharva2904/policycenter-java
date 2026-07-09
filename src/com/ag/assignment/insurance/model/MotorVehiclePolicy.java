package com.ag.assignment.insurance.model;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;

public class MotorVehiclePolicy extends Policy {

    private final MotorVehicle motorVehicle;
    private final int policyDurationInYears;


    public MotorVehiclePolicy(PolicyHolder policyHolder, MotorVehicle vehicle) {
        super();
        this.setPolicyHolder(policyHolder);
        this.motorVehicle = vehicle;
        policyDurationInYears = 1;
    }

    public MotorVehicle getMotorVehicle() {
        return motorVehicle;
    }

    @Override
    public void activate() {
        if (this.getPolicyStatus() != PolicyStatus.ACTIVE) {

            setPolicyStatus(PolicyStatus.ACTIVE);
            setUpdatedAt(Instant.now());
            setStartDate(LocalDate.now());
            setEndDate(LocalDate.now().plusYears(policyDurationInYears));
        } else {
            throw new RuntimeException("Policy is already active! Cannot activate policy more than once!!");
        }
    }

    @Override
    public void cancel() {
        if(this.getPolicyStatus() != PolicyStatus.CANCELLED) {
            setPolicyStatus(PolicyStatus.CANCELLED);
            setUpdatedAt(Instant.now());
        }
        else{
            throw new RuntimeException("Policy is already cancelled! Cannot cancel policy more than once!!");

        }
    }

}



