package com.wipfli.training.service;

import com.wipfli.training.model.Policy;
import com.wipfli.training.model.PremiumCalculable;
import com.wipfli.training.store.AgeGroupFactorStore;

import java.math.BigDecimal;

public class NoClaimBonusCalculator implements PremiumCalculable {
    private static final Double COST_PER_CLAIM = 150.0;
    private final AgeGroupFactorStore ageGroupFactorStore;

    public NoClaimBonusCalculator() {
        this.ageGroupFactorStore = new AgeGroupFactorStore();
    }

    @Override
    public Double calculatePremium(Policy policy) {
        if (policy == null || policy.getVehicleType() == null || policy.getPolicyHolder() == null) {
            return 0.0;
        }

        int age = (int) policy.getPolicyHolder().getAge();
        int numberOfClaims = policy.getClaimsCount();

        Double basePremium = Double.valueOf(policy.getVehicleType().getBasePremium());
        Double ageFactor = Double.valueOf(ageGroupFactorStore.getFactor(age));
        Double crashCost = COST_PER_CLAIM * numberOfClaims;

        Double ageSurcharge = basePremium * (ageFactor);
        Double totalPremium = basePremium + (ageSurcharge) + (crashCost);

        if (numberOfClaims == 0)
            totalPremium = totalPremium - (totalPremium * (0.1));

        return totalPremium;
    }


}
