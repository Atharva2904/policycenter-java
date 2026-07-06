package com.ag.assignment.insurance.repository;

import com.ag.assignment.insurance.model.VehicleType;

import java.util.Map;

public class VehicleFactorRepository {

    private final Map<VehicleType, Double> vehicleFactors = Map.of(
            VehicleType.SEDAN, 1.8,
            VehicleType.SUV, 1.5,
            VehicleType.CUV, 1.2,
            VehicleType.XUV, 1.7,
            VehicleType.VAN, 1.1
    );

    public double getVehicleFactor(VehicleType vehicleType) {
        return vehicleFactors.get(vehicleType);
    }

}
