package com.ag.assignment.insurance.service;

import com.ag.assignment.insurance.model.Policy;
import com.ag.assignment.insurance.model.VehicleType;


public class PolicyValidator {

    public PolicyValidator() {
    }

    public static void validateAge(int age){
        if(age < 18 || age > 100)
            throw new IllegalArgumentException(
                    "Age must be between 18 and 100");

    }

    public static boolean validate(Policy policy) {
        if (policy == null) throw new IllegalArgumentException("Policy object cannot be null!");


        if(policy.getVehicleType() == null) throw new IllegalArgumentException("""
                Vehicle type cannot be null! Enter one of the following choices:
                1. CAR\t2. TRUCK\t3. BIKE
                """) ;

        if(policy.getPreviousClaimsCount() < 0)
            throw new IllegalArgumentException("No. of claims must be greater than or equal to 0!! Please enter a valid value.");


        return true;
    }
}
