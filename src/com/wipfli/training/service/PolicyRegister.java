package com.wipfli.training.service;

import com.wipfli.training.exception.DuplicatePolicyNumberException;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.model.Policy;
import com.wipfli.training.model.PremiumCalculable;
import com.wipfli.training.model.VehicleType;

import java.time.LocalDate;
import java.util.*;

public class PolicyRegister {
    private final Hashtable<String, Policy> policyHashtable;
    private final Hashtable<String, List<Policy>> policiesByCustomerName;
    private final EnumMap<VehicleType, List<Policy>> policiesByVehicleType;
    private final TreeMap<LocalDate, List<Policy>> policiesByExpiryDate;


    public PolicyRegister() {
        policyHashtable = new Hashtable<>();
        policiesByCustomerName = new Hashtable<>();
        policiesByVehicleType = new EnumMap<>(VehicleType.class);
        policiesByExpiryDate = new TreeMap<>();
    }


    public void add(Policy policy) {
        String policyNumber = policy.getPolicyNumber();
        String customerName = policy.getPolicyHolder().getDisplayName();
        VehicleType vehicleType = policy.getVehicleType();
        LocalDate policyExpiryDate = policy.getExpiryDate();

        if (policyHashtable.containsKey(policyNumber)) {
            throw new DuplicatePolicyNumberException(policyNumber,
                    String.format("DUPLICATE POLICY ERROR: Policy with number %s already exists!!", policyNumber));
        }


        policyHashtable.put(policy.getPolicyNumber(), policy);
        policiesByCustomerName.computeIfAbsent(customerName, k -> new ArrayList<>()).add(policy);

        /*
         Requires TWO lookups (containsKey + put)
        if (!policiesByCustomerName.containsKey(customerName)) {
            policiesByCustomerName.put(customerName, new ArrayList<>());
        }
        policiesByCustomerName.get(customerName).add(policy); // Requires a THIRD lookup
        */

        policiesByVehicleType.computeIfAbsent(vehicleType, k -> new ArrayList<>()).add(policy);
        policiesByExpiryDate.computeIfAbsent(policyExpiryDate, k -> new ArrayList<>()).add(policy);

    }

    public Policy findByNumber(String policyNumber) throws PolicyNotFoundException {

        Policy policy = policyHashtable.get(policyNumber);

        if (policy == null) {
            throw new PolicyNotFoundException(
                    policyNumber,
                    String.format("Policy with number %s not found!", policyNumber)
            );
        }

        return policy;

    }

    public List<Policy> findByCustomer(String customerName)  {
        List<Policy> policyListGroupedByCustomer = policiesByCustomerName.get(customerName);

        if (policyListGroupedByCustomer == null) {
            System.out.printf("[LOG]: No policy records found for the customer '%s'%n", customerName);
            return new ArrayList<>();
        }

        return policyListGroupedByCustomer;
    }

    public List<Policy> findByVehicleType(VehicleType vehicleType) {
        List<Policy> policyListGroupedByVehicleType = policiesByVehicleType.get(vehicleType);

        if (policyListGroupedByVehicleType == null) {
            System.out.printf("[LOG]: No policy records found for the vehicle type '%s'%n", vehicleType);
            return new ArrayList<>();
        }

        return policyListGroupedByVehicleType;
    }

    public List<Policy> findExpiringWithinDays(int days, LocalDate todayDate) {
        LocalDate targetDate = todayDate.plusDays(days);

        List<Policy> policiesExpiringWithinDays = new ArrayList<>();

        // Since we're using a TreeMap for storing the List<Policy> by expiry dates, we can efficiently retrieve the policies that are expiring within the specified range of dates using the subMap method.
        // This method returns a view of the portion of this map whose keys range from currentDate (inclusive) to targetDate (inclusive).
        // We then iterate over the values of this subMap and add all the policies to our result list.

        policiesByExpiryDate.subMap(todayDate, true, targetDate, true)
                .values()
                .forEach(policiesExpiringWithinDays::addAll);


        return policiesExpiringWithinDays;

    }

    public Hashtable<VehicleType, Double> totalPremiumByVehicleType() {
        PremiumCalculable premiumCalculator = new NoClaimBonusCalculator();
        Hashtable<VehicleType, Double> totalPremiumGroupedByVehicleType = new Hashtable<>();

        for (Map.Entry<VehicleType, List<Policy>> entry : policiesByVehicleType.entrySet()) {
            VehicleType vehicleType = entry.getKey();
            List<Policy> policyList = entry.getValue();

            double totalPremiumSumByVehicleType = 0;
            for (Policy p : policyList) {
                totalPremiumSumByVehicleType += premiumCalculator.calculatePremium(p);
            }
            totalPremiumGroupedByVehicleType.put(vehicleType, totalPremiumSumByVehicleType);
        }


        return totalPremiumGroupedByVehicleType;
    }
}
