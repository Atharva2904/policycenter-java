package com.ag.assignment.insurance.service;

import com.ag.assignment.insurance.model.*;
import com.ag.assignment.insurance.repository.AgeGroupFactorRepository;
import com.ag.assignment.insurance.repository.VehicleFactorRepository;

import java.math.BigDecimal;

public class PremiumCalculationService implements PremiumCalculable {

    private final VehicleFactorRepository vehicleFactorRepository;
    private final AgeGroupFactorRepository ageGroupFactorRepository;

    private static final BigDecimal BASE_RATE = new BigDecimal("5000.00");

    public PremiumCalculationService(VehicleFactorRepository vehicleFactorRepository, AgeGroupFactorRepository ageGroupFactorRepository) {
        this.ageGroupFactorRepository = ageGroupFactorRepository;
        this.vehicleFactorRepository = vehicleFactorRepository;
    }

    public BigDecimal calculatePremium(MotorVehiclePolicy policy){
        MotorVehicle motorVehicle = policy.getMotorVehicle();
        PolicyHolder policyHolder = policy.getPolicyHolder();
        VehicleType vehicleType = motorVehicle.getVehicleType();

        int age = (int) policyHolder.getAge();
        int previousClaims = policyHolder.getPreviousClaims();

        double vehicleFactor = vehicleFactorRepository.getVehicleFactor(vehicleType);
        double ageFactor = ageGroupFactorRepository.getFactor(age);

        return policy.getBasePremium().multiply(BigDecimal.valueOf(vehicleFactor)).multiply(BigDecimal.valueOf(ageFactor));
    }
}
