package com.wipfli.training.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("CAR")
public class CarPolicy extends Policy{

    @Column(name = "registration_number", unique = true, length = 56)

    private String registrationNumber;

    public CarPolicy(String policyNumber, PolicyHolder policyHolder, String registrationNumber, LocalDate expiryDate){
        this(policyNumber, policyHolder, registrationNumber, 0, expiryDate);
    }

    public CarPolicy(String policyNumber, PolicyHolder policyHolder, String registrationNumber, int previousClaimsCount, LocalDate expiryDate){
        super(policyNumber, policyHolder, VehicleType.CAR, previousClaimsCount, expiryDate);
        this.registrationNumber = registrationNumber;
    }


    // No-arg constructor required by JPA/Hibernate
    protected CarPolicy() {
        super();
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
