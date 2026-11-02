package com.wipfli.training.dto;

import com.wipfli.training.model.PolicyStatus;
import com.wipfli.training.model.VehicleType;

import java.time.LocalDate;


public record PolicyResponse(
        String policyNumber,
        PolicyStatus policyStatus,
        VehicleType vehicleType,
        int claimsCount,
        String holderName,
        LocalDate expiryDate,
        APIStatus apiStatus
) {
    public PolicyResponse(
            String policyNumber,
            PolicyStatus policyStatus,
            VehicleType vehicleType,
            int claimsCount,
            String holderName,
            LocalDate expiryDate
    ) {
        this(policyNumber, policyStatus, vehicleType, claimsCount, holderName, expiryDate,
                APIStatus.ok("Policy processed successfully"));
    }

    public static PolicyResponse created(String policyNumber, PolicyStatus policyStatus,
                                        VehicleType vehicleType, int claimsCount,
                                        String holderName, LocalDate expiryDate) {
        return new PolicyResponse(policyNumber, policyStatus, vehicleType, claimsCount,
                holderName, expiryDate, APIStatus.created("Policy created successfully"));
    }

    public static PolicyResponse retrieved(String policyNumber, PolicyStatus policyStatus,
                                           VehicleType vehicleType, int claimsCount,
                                           String holderName, LocalDate expiryDate) {
        return new PolicyResponse(policyNumber, policyStatus, vehicleType, claimsCount,
                holderName, expiryDate, APIStatus.ok("Policy retrieved successfully"));
    }

    public static PolicyResponse updated(String policyNumber, PolicyStatus policyStatus,
                                        VehicleType vehicleType, int claimsCount,
                                        String holderName, LocalDate expiryDate,
                                        String message) {
        return new PolicyResponse(policyNumber, policyStatus, vehicleType, claimsCount,
                holderName, expiryDate, APIStatus.ok(message));
    }
}
