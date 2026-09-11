package com.wipfli.training.service;

import com.wipfli.training.exception.DuplicatePolicyNumberException;
import com.wipfli.training.exception.InvalidPolicyDataException;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.model.Policy;
import com.wipfli.training.model.PremiumCalculable;
import com.wipfli.training.model.VehicleType;
import com.wipfli.training.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PolicyRegister {
    private PremiumCalculable premiumCalculator;
    private final PolicyRepository repository;


    public PolicyRegister(
            @Qualifier("noClaimBonusCalculator")
            PremiumCalculable premiumCalculator, PolicyRepository repository) {
        this.premiumCalculator = premiumCalculator;
        this.repository = repository;
    }

    public void add(Policy policy) {
        repository.save(policy);
    }

    public Optional<Policy> findByNumber(String policyNumber) {
        return repository.findByPolicyNumber(policyNumber);

    }

    public List<Policy> findByCustomer(String customerName) {
        /*
            Optional.ofNullable() method is used to handle the null values gracefully without the risk of encountering a NullPointerException.
            If .get() returns a null value, the Optional.ofNullable() wraps it inside an empty Optional Object
            The orElse() detects that Optional is Empty and replaces the empty value with an immutable Empty List
         */

        List<Policy> policyListGroupedByCustomer = repository.findByCustomerName(customerName);

        return policyListGroupedByCustomer;
    }

    public List<Policy> findByVehicleType(VehicleType vehicleType) {
        List<Policy> policyListGroupedByVehicleType = repository.findByVehicleType(vehicleType);

        return policyListGroupedByVehicleType;
    }

    public double getPremium(String policyNumber) throws PolicyNotFoundException {
        Policy policy = repository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new PolicyNotFoundException(
                        policyNumber,
                        "No policy found with number: " + policyNumber
                ));
        return premiumCalculator.calculatePremium(policy);
    }

    public List<Policy> findExpiringWithinDays(int days) {


        List<Policy> policiesExpiringWithinDays = repository.findByExpiryDateRange(days);

        return policiesExpiringWithinDays;

    }

    public Hashtable<VehicleType, Double> totalPremiumByVehicleType() {
        Hashtable<VehicleType, Double> totalPremiumGroupedByVehicleType = new Hashtable<>();

        /*
            .sum() is a method provided by the Stream API in Java. It is used to calculate the sum of a stream of numeric values.
            The Collections objects in Java do not contain this method. Hence, we need to convert these collections objects to a Stream object
            specifically before we can operate on them. It is done using .stream() method

         */

        EnumMap<VehicleType, List<Policy>> policiesByVehicleType = new EnumMap<>(VehicleType.class);
        repository.countPoliciesByVehicleType().forEach((vehicleType, count) ->
                policiesByVehicleType.put(vehicleType, repository.findByVehicleType(vehicleType))
        );


        policiesByVehicleType.forEach((vehicleType, policies) ->
                totalPremiumGroupedByVehicleType.put(vehicleType, policies.stream()
                        .mapToDouble(p -> premiumCalculator.calculatePremium(p))
                        .sum()
                )
        );


        return totalPremiumGroupedByVehicleType;
    }

    public Hashtable<VehicleType, Integer> countPoliciesByVehicleType() {

        return repository.countPoliciesByVehicleType();
    }

    public Optional<String> getCustomerWithMostPolicies() {

        return repository.getCustomerWithMostPolicies();

    }

    public Map<String, Double> getTop5PoliciesByPremium() {
        Map<String, Policy> policyMap = repository.findAll();

        return policyMap.values().stream()
                .map(policy -> Map.entry(policy.getPolicyNumber(), premiumCalculator.calculatePremium(policy)))
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }



/*
    public List<String> getCustomersWithMatchingPattern(String customerName) {
        List<String> matchingCustomerNames = new ArrayList<>();
        List<Policy> policyListForGivenCustomer = findByCustomer(customerName);


        Map<Class<? extends Policy>, Long> targetPattern = policyListForGivenCustomer.stream()
                .collect(Collectors.groupingBy(
                        Policy::getClass,
                        Collectors.counting()
                ));


        repository.findAll().forEach((customer, policies) -> {
            Map<Class<? extends Policy>, Long> currentPattern = policies.stream()
                    .collect(Collectors.groupingBy(
                            Policy::getClass,
                            Collectors.counting()
                    ));

            if (currentPattern.equals(targetPattern)) matchingCustomerNames.add(customer);

        });


        return matchingCustomerNames;
    }

*/

}
