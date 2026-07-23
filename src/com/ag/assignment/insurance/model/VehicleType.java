package com.ag.assignment.insurance.model;

public enum VehicleType {
    CAR(500, 1.4, 1),
    TRUCK(800, 1.7, 2),
    BIKE(300, 1.2, 3);

    private final double BASE_PREMIUM;
    private final double factor;
    private final int code;


    VehicleType(double basePremium, double factor, int code){
        this.BASE_PREMIUM = basePremium;
        this.factor = factor;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public double getFactor() {
        return factor;
    }

    public double getBasePremium() {
        return BASE_PREMIUM;
    }

    public static VehicleType getVehicleFromChoice(int choice){
        for(VehicleType vehicleType: values()){
            if(vehicleType.code == choice) return vehicleType;
        }

        throw new IllegalArgumentException("Invalid Vehicle Type code provided!");
    }
}