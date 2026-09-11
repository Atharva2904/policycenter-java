package com.wipfli.training.repository;

import com.wipfli.training.model.Policy;
import com.wipfli.training.model.VehicleType;

import java.util.*;

public interface PolicyRepository {

    void save(Policy policy);
    Map<String, Policy> findAll();
    Optional<Policy> findByPolicyNumber(String policyNumber);
    List<Policy> findByCustomerName(String customerName);
    List<Policy> findByVehicleType(VehicleType vehicleType);
    List<Policy> findByExpiryDateRange(int days);
    Hashtable<VehicleType, Integer> countPoliciesByVehicleType();
    Optional<String> getCustomerWithMostPolicies();

}
