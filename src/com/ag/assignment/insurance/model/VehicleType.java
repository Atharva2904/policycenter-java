package com.ag.assignment.insurance.model;

public enum VehicleType {
    SUV(5000, 1.8),
    XUV(7000, 1.5),
    CUV(4500, 1.2),
    SEDAN(6500, 1.7),
    VAN(4000, 1.1),
    CAR(500, 1.4),
    TRUCK(800, 1.7),
    BIKE(300, 1.2);

    private final double BASE_PREMIUM;
    private final double factor;


    VehicleType(double basePremium, double factor){
        this.BASE_PREMIUM = basePremium;
        this.factor = factor;
    }

    public double getFactor() {
        return factor;
    }

    public double getBasePremium() {
        return BASE_PREMIUM;
    }
}