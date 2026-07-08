package com.ag.assignment.insurance.model;

public enum VehicleType {
    SUV(5000, 1.8, 1),
    XUV(7000, 1.5, 2),
    CUV(4500, 1.2, 3),
    SEDAN(6500, 1.7, 4),
    VAN(4000, 1.1, 5),
    CAR(500, 1.4, 6),
    TRUCK(800, 1.7, 7),
    BIKE(300, 1.2, 8);

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