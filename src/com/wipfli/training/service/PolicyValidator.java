package com.wipfli.training.service;

import com.wipfli.training.exception.InvalidPolicyDataException;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.model.Policy;

public class PolicyValidator {

    public PolicyValidator() {
    }

    public static void validateAge(int age){
        if(age < 18 || age > 100)
            throw new IllegalArgumentException(
                    "Age must be between 18 and 100");

    }

    public static void validate(Policy policy) throws PolicyNotFoundException {
        if (policy == null) throw new PolicyNotFoundException("UNKNOWN", "Policy object cannot be null!");

        String policyNumber = policy.getPolicyNumber();


        if(policy.getVehicleType() == null) throw new InvalidPolicyDataException(policyNumber, """
                Vehicle type cannot be null! Enter one of the following choices:
                1. CAR\t2. TRUCK\t3. BIKE
                """) ;

        if(policy.getClaimsCount() < 0)
            throw new InvalidPolicyDataException(policyNumber, "No. of claims must be greater than or equal to 0!! Please enter a valid value.");

    }
}
