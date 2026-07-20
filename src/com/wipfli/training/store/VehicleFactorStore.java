package com.wipfli.training.store;

import com.wipfli.training.model.VehicleType;

import java.util.Map;

public class VehicleFactorStore {

    private final Map<VehicleType, Double> vehicleFactors = Map.of(
            VehicleType.TRUCK, 1.8,
            VehicleType.CAR, 1.5,
            VehicleType.BIKE, 1.2
    );

    public double getVehicleFactor(VehicleType vehicleType) {
        return vehicleFactors.get(vehicleType);
    }

}
