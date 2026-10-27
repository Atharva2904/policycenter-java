package com.wipfli.training.dto;

import com.wipfli.training.model.PolicyStatus;
import com.wipfli.training.model.VehicleType;

import java.time.Instant;
import java.time.LocalDate;

public record EnhancedPolicyResponse(
        String policyNumber,
        PolicyStatus policyStatus,
        VehicleType vehicleType,
        String holderName,
        int holderAge,
        String holderState,
        int claimsCount,
        Instant createdAt,
        LocalDate startDate,
        LocalDate expiryDate,
        Double premium,
        String registrationNumber,
        Integer engineCC,
        Double loadCapacityTons,
        APIStatus apiStatus
) {
    public EnhancedPolicyResponse(
            String policyNumber,
            PolicyStatus policyStatus,
            VehicleType vehicleType,
            String holderName,
            int holderAge,
            String holderState,
            int claimsCount,
            Instant createdAt,
            LocalDate startDate,
            LocalDate expiryDate,
            Double premium,
            String registrationNumber,
            Integer engineCC,
            Double loadCapacityTons
    ) {
        this(policyNumber, policyStatus, vehicleType, holderName, holderAge, holderState,
                claimsCount, createdAt, startDate, expiryDate, premium,
                registrationNumber, engineCC, loadCapacityTons,
                APIStatus.success("Policy processed successfully"));
    }
}

