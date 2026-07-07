package com.ag.assignment.insurance.service;

import com.ag.assignment.insurance.model.MotorVehiclePolicy;

import java.math.BigDecimal;

public interface PremiumCalculable {

    BigDecimal calculatePremium(MotorVehiclePolicy policy);

}
