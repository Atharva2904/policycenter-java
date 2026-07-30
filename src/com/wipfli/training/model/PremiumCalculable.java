package com.wipfli.training.model;

import java.math.BigDecimal;

public interface PremiumCalculable {

    BigDecimal calculatePremium(Policy policy);

}
