package com.wipfli.training.model;

public class BikePolicy extends Policy {
    private int engineCC;

    public BikePolicy(String policyNumber, PolicyHolder policyHolder, int engineCC){
        super(policyNumber, policyHolder, VehicleType.BIKE);
        this.engineCC = engineCC;
    }

    public int getEngineCC() {
        return engineCC;
    }

    @Override
    public String getPolicyDetails() {
        return String.format("Policy: %s| Customer Name: %s| Status: %s| Vehicle: %s| Engine CC: %s",
                getPolicyNumber(), getPolicyHolder().getDisplayName(), getPolicyStatus(), getVehicleType(), getEngineCC()
        );
    }
}
