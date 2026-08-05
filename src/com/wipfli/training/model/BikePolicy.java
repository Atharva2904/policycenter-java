package com.wipfli.training.model;


import java.time.LocalDate;

public class BikePolicy extends Policy {
    private int engineCC;

    public BikePolicy(String policyNumber, PolicyHolder policyHolder, int engineCC, LocalDate expiryDate){
        super(policyNumber, policyHolder, VehicleType.BIKE, expiryDate);
        this.engineCC = engineCC;
    }

    public int getEngineCC() {
        return engineCC;
    }

    @Override
    public String getPolicyDetails() {
        return getCommonDetails() + String.format("""
            Vehicle Type:        %s
            Engine CC:           %s
            ======================================
            """,
                VehicleType.BIKE,
                getEngineCC()
        );
    }
}
