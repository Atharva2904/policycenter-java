package com.wipfli.training.service;

import com.wipfli.training.model.Policy;
import com.wipfli.training.model.PremiumCalculable;
import com.wipfli.training.store.AgeGroupFactorStore;

import java.math.BigDecimal;

public class StandardPremiumCalculator implements PremiumCalculable {

    private static final BigDecimal COST_PER_CLAIM = new BigDecimal("150.0");
    private final AgeGroupFactorStore ageGroupFactorStore;

    public StandardPremiumCalculator(AgeGroupFactorStore ageGroupFactorStore){
        this.ageGroupFactorStore = ageGroupFactorStore;
    }


    @Override
    public BigDecimal calculatePremium(Policy policy) {
        if(policy == null || policy.getVehicleType() == null || policy.getPolicyHolder() == null){
            return BigDecimal.ZERO;
        }

        int age = (int) policy.getPolicyHolder().getAge();
        int numberOfClaims = policy.getPreviousClaimsCount();

        BigDecimal basePremium = BigDecimal.valueOf(policy.getVehicleType().getBasePremium());
        BigDecimal ageFactor = BigDecimal.valueOf(ageGroupFactorStore.getFactor(age));
        BigDecimal crashCost = COST_PER_CLAIM.multiply(BigDecimal.valueOf(numberOfClaims));

        BigDecimal ageSurcharge = basePremium.multiply(ageFactor);
        return basePremium.add(ageSurcharge).add(crashCost);
    }
}
