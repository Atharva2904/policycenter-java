package com.wipfli.training.service;

import com.wipfli.training.model.Policy;
import com.wipfli.training.model.PremiumCalculable;
import com.wipfli.training.store.AgeGroupFactorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("noClaimBonusCalculator")
public class NoClaimBonusCalculator implements PremiumCalculable {
    private final PremiumCalculable STANDARD_PREMIUM_CALCULATOR;

    public NoClaimBonusCalculator(
            @Qualifier("standardPremiumCalculator")
            PremiumCalculable standardPremiumCalculator) {
        this.STANDARD_PREMIUM_CALCULATOR = standardPremiumCalculator;
    }

    @Override
    public Double calculatePremium(Policy policy) {
        if (policy == null || policy.getVehicleType() == null || policy.getPolicyHolder() == null) {
            return 0.0;
        }

        /*
            This is an example of the Decorator Pattern. The NoClaimBonusCalculator class here acts as a decorator that adds additional functionality to the StandardPremiumCalculator class.
            It calculates the total premium by first calculating the standard premium using the StandardPremiumCalculator, and then applying a 10% discount if there are no claims.
         */

        Double totalPremium = STANDARD_PREMIUM_CALCULATOR.calculatePremium(policy);
        int numberOfClaims = policy.getClaimsCount();

        if (numberOfClaims == 0)
            totalPremium = totalPremium - (totalPremium * (0.1));

        return totalPremium;
    }


}
