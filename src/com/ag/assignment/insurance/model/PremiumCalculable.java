package com.ag.assignment.insurance.model;

import java.math.BigDecimal;

public interface PremiumCalculable {

    BigDecimal calculatePremium(Policy policy);

}
