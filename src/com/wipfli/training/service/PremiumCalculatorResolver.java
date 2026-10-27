package com.wipfli.training.service;

import com.wipfli.training.model.Policy;
import com.wipfli.training.model.PremiumCalculable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PremiumCalculatorResolver {
    private final PremiumCalculable standardPremiumCalculator;
    private final PremiumCalculable noClaimBonusCalculator;

    public PremiumCalculatorResolver(
            @Qualifier("standardPremiumCalculator") PremiumCalculable standardPremiumCalculator,
            @Qualifier("noClaimBonusCalculator") PremiumCalculable noClaimBonusCalculator) {
        this.standardPremiumCalculator = Objects.requireNonNull(standardPremiumCalculator,
                "Standard premium calculator must not be null.");
        this.noClaimBonusCalculator = Objects.requireNonNull(noClaimBonusCalculator,
                "No-claim bonus calculator must not be null.");
    }

    public PremiumCalculable resolve(Policy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Policy cannot be null.");
        }

        if (policy.getPolicyHolder() == null) {
            throw new IllegalArgumentException("Policy holder cannot be null for premium calculation.");
        }

        if (policy.getVehicleType() == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null for premium calculation.");
        }

        if (policy.getClaimsCount() < 0) {
            throw new IllegalArgumentException("Claims count cannot be negative for premium calculation.");
        }

        if (policy.getClaimsCount() == 0) {
            return noClaimBonusCalculator;
        }

        return standardPremiumCalculator;
    }
}

