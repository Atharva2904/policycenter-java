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
                APIStatus.success("Policy processed successfully"));
    }
}
