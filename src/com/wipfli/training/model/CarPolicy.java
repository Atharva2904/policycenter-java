package com.wipfli.training.model;


import java.time.LocalDate;

public class CarPolicy extends Policy{

    private String registrationNumber;

    public CarPolicy(String policyNumber, PolicyHolder policyHolder, String registrationNumber, LocalDate expiryDate){
        super(policyNumber, policyHolder, VehicleType.CAR, expiryDate);
        this.registrationNumber = registrationNumber;
    }


    public String getRegistrationNumber() {
        return registrationNumber;
    }


    @Override
    public String getPolicyDetails() {
        return getCommonDetails() + String.format("""
            Vehicle Type:        %s
            Registration No:     %s
            ======================================
            """,
                VehicleType.CAR,
                getRegistrationNumber()
        );
    }

}
