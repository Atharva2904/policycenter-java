package com.ag.assignment.insurance.service;

import com.ag.assignment.insurance.model.Policy;
import com.ag.assignment.insurance.model.PremiumCalculable;
import com.ag.assignment.insurance.repository.AgeGroupFactorRepository;

import java.math.BigDecimal;

public class NoClaimBonusCalculator implements PremiumCalculable {
    private static final BigDecimal COST_PER_CLAIM = new BigDecimal("150.0");
    private final AgeGroupFactorRepository ageGroupFactorRepository;

    public NoClaimBonusCalculator(AgeGroupFactorRepository ageGroupFactorRepository) {
        this.ageGroupFactorRepository = ageGroupFactorRepository;
    }

    @Override
    public BigDecimal calculatePremium(Policy policy) {
        if (policy == null || policy.getVehicleType() == null || policy.getPolicyHolder() == null) {
            return BigDecimal.ZERO;
        }

        int age = (int) policy.getPolicyHolder().getAge();
        int numberOfClaims = policy.getPreviousClaimsCount();

        BigDecimal basePremium = BigDecimal.valueOf(policy.getVehicleType().getBasePremium());
        BigDecimal ageFactor = BigDecimal.valueOf(ageGroupFactorRepository.getFactor(age));
        BigDecimal crashCost = COST_PER_CLAIM.multiply(BigDecimal.valueOf(numberOfClaims));

        BigDecimal ageSurcharge = basePremium.multiply(ageFactor);
        BigDecimal totalPremium = basePremium.add(ageSurcharge).add(crashCost);

        if (numberOfClaims == 0)
            totalPremium = totalPremium.subtract(totalPremium.multiply(BigDecimal.valueOf(0.1)));

        return totalPremium;
    }


}
