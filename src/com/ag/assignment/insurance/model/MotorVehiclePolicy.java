package com.ag.assignment.insurance.model;

import java.math.BigInteger;

public class MotorVehiclePolicy extends Policy{

    private final MotorVehicle motorVehicle;


    public MotorVehiclePolicy(BigInteger policyNumber){
        super(policyNumber);
        this.motorVehicle = null;
    }

    public MotorVehiclePolicy(PolicyHolder policyHolder, MotorVehicle vehicle){
        super();
        this.setPolicyHolder(policyHolder);
        this.motorVehicle = vehicle;
    }

    public MotorVehicle getMotorVehicle() {
        return motorVehicle;
    }


}
