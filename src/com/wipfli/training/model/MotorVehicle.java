package com.wipfli.training.model;

public class MotorVehicle {
    private VehicleType vehicleType;
    private final String vehicleNumber;

    public MotorVehicle(String vehicleNumber, VehicleType vehicleType){
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }


}
