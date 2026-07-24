package com.wipfli.training.model;

public class CarPolicy extends Policy{

    private String registrationNumber;

    public CarPolicy(String policyNumber, PolicyHolder policyHolder, String registrationNumber){
        super(policyNumber, policyHolder, VehicleType.CAR);
        this.registrationNumber = registrationNumber;
    }


    public String getRegistrationNumber() {
        return registrationNumber;
    }


    @Override
    public String getPolicyDetails() {
        return String.format("Policy: %s| Customer Name: %s| Status: %s| Vehicle: %s| Registration Number: %s",
                getPolicyNumber(), getPolicyHolder().getDisplayName(), getPolicyStatus(), VehicleType.CAR, getRegistrationNumber()
                );
    }

}
