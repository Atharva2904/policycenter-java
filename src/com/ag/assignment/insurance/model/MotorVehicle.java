package com.ag.assignment.insurance.model;

import java.math.BigDecimal;

public class MotorVehicle {
    private VehicleType vehicleType;
    private final String vehicleNumber;
    private final String fuelType;

    public MotorVehicle(String vehicleNumber, VehicleType vehicleType, String fuelType){
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.fuelType = fuelType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }


    public String getFuelType() {
        return fuelType;
    }


    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }



}
