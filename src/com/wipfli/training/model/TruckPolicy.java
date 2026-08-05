package com.wipfli.training.model;


import com.wipfli.training.exception.InvalidPolicyDataException;

import java.time.LocalDate;

/**
 * This is a concrete implementation of the abstract class {@link Policy}
 * It consists of one overloaded method: {@code getPolicyDetails()}
 * Additionally it consists of another attribute: {@code loadCapacityTons}
 *
 */
public class TruckPolicy extends Policy {
    private double loadCapacityTons;

    // A Constant variable that indicates the minimum age limit for a truck driver.
    private static final int TRUCK_AGE_LIMIT = 21;

    public TruckPolicy(String policyNumber, PolicyHolder policyHolder, double loadCapacityTons, LocalDate expiryDate) {
        super(policyNumber, policyHolder, VehicleType.TRUCK, expiryDate);

        // Special Case: For a truck-driver, the minimum age limit is 21.
        if (policyHolder.getAge() < TRUCK_AGE_LIMIT) {
            throw new InvalidPolicyDataException(policyNumber,
                    String.format("Truck policy requires driver to be of minimum 21 years old. This driver is %d years old", policyHolder.getAge()));
        }

        this.loadCapacityTons = loadCapacityTons;
    }

    public double getLoadCapacityTons() {
        return loadCapacityTons;
    }

    @Override
    public String getPolicyDetails() {
        return getCommonDetails() + String.format("""
            Vehicle Type:            %s
            Load Capacity (in Tons): %f
            ======================================
            """,
                VehicleType.TRUCK,
                getLoadCapacityTons()
        );
    }
}
