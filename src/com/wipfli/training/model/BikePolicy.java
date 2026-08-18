package com.wipfli.training.model;


import java.time.LocalDate;

public class BikePolicy extends Policy {
    private int engineCC;

    public BikePolicy(String policyNumber, PolicyHolder policyHolder, int engineCC, LocalDate expiryDate){
        this(policyNumber, policyHolder, engineCC, 0, expiryDate);
    }

    public BikePolicy(String policyNumber, PolicyHolder policyHolder, int engineCC, int previousClaimsCount, LocalDate expiryDate){
        super(policyNumber, policyHolder, VehicleType.BIKE, previousClaimsCount, expiryDate);
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
