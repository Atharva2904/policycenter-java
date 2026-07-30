package com.wipfli.training.model;

/**
 * This is a concrete implementation of the abstract class {@link Policy}
 * It consists of one overloaded method: {@code getPolicyDetails()}
 * Additionally it consists of another attribute: {@code loadCapacityTons}
 *
 */
public class TruckPolicy extends Policy{
    private double loadCapacityTons;

    public TruckPolicy(String policyNumber, PolicyHolder policyHolder, double loadCapacityTons){
        super(policyNumber, policyHolder, VehicleType.TRUCK);
        this.loadCapacityTons = loadCapacityTons;
    }

    public double getLoadCapacityTons() {
        return loadCapacityTons;
    }

    @Override
    public String getPolicyDetails() {
        return String.format("Policy: %s| Customer Name: %s| Status: %s| Vehicle: %s| Load Capacity (in Tons): %f",
                getPolicyNumber(), getPolicyHolder().getDisplayName(), getPolicyStatus(), getVehicleType(), getLoadCapacityTons()
        );
    }
}
