package com.wipfli.training.dto;

import com.wipfli.training.model.State;
import com.wipfli.training.model.VehicleType;

public record PolicyRequest(
        String policyNumber,
        String holderFirstName,
        String holderLastName,
        int holderAge,
        State holderState,
        VehicleType vehicleType,
        String expiryDate,
        int claimsCount,
        String registrationNumber,
        Integer engineCC,
        Double loadCapacity
) {}