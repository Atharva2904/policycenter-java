package com.ag.assignment.insurance.service;

import com.ag.assignment.insurance.model.*;
import com.ag.assignment.insurance.repository.AgeGroupFactorRepository;
import com.ag.assignment.insurance.repository.VehicleFactorRepository;

import java.math.BigDecimal;

public class MotorVehiclePremiumService implements PremiumCalculable {

    private static final BigDecimal COST_PER_CRASH = new BigDecimal("150.00");
    private final AgeGroupFactorRepository ageGroupFactorRepository;


    public MotorVehiclePremiumService(AgeGroupFactorRepository ageGroupFactorRepository) {
        this.ageGroupFactorRepository = ageGroupFactorRepository;
    }

    public BigDecimal calculatePremium(MotorVehiclePolicy policy) {
        if (policy == null || policy.getMotorVehicle() == null || policy.getPolicyHolder() == null) {
            return BigDecimal.ZERO;
        }
        MotorVehicle motorVehicle = policy.getMotorVehicle();
        PolicyHolder policyHolder = policy.getPolicyHolder();
        VehicleType vehicleType = motorVehicle.getVehicleType();

        int age = (int) policyHolder.getAge();
        int numberOfCrashes = policyHolder.getNumberOfCrashes();

        BigDecimal basePremium = BigDecimal.valueOf(vehicleType.getBasePremium());
        BigDecimal ageFactor = BigDecimal.valueOf(ageGroupFactorRepository.getFactor(age));
        BigDecimal crashCost = COST_PER_CRASH.multiply(BigDecimal.valueOf(numberOfCrashes));

        BigDecimal ageSurcharge = basePremium.multiply(ageFactor);
        return basePremium.add(ageSurcharge).add(crashCost);
    }

}
