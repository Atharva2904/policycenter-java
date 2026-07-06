package com.ag.assignment.insurance.model;

import java.math.BigDecimal;

public class MotorVehicle {
    private VehicleType vehicleType;
    private BigDecimal vehicleAge;
    private final String vehicleNumber;
    private final String fuelType;
    private BigDecimal marketValue;

    public MotorVehicle(String vehicleNumber, VehicleType vehicleType, String fuelType, BigDecimal marketValue){
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.fuelType = fuelType;
        this.marketValue = marketValue;

        this.vehicleAge = BigDecimal.ZERO;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public BigDecimal getVehicleAge() {
        return vehicleAge;
    }

    public String getFuelType() {
        return fuelType;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }



    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }


    public void setVehicleAge(BigDecimal vehicleAge) {
        this.vehicleAge = vehicleAge;
    }


}
