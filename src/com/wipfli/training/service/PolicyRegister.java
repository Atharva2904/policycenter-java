package com.wipfli.training.service;

import com.wipfli.training.exception.DuplicatePolicyNumberException;
import com.wipfli.training.exception.InvalidPolicyDataException;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.model.Policy;
import com.wipfli.training.model.VehicleType;
import com.wipfli.training.repository.PolicyRepository;
import com.wipfli.training.repository.VehicleTypeCount;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PolicyRegister {
    private final PremiumCalculatorResolver premiumCalculatorResolver;
    private final PolicyRepository repository;
    private final LocalDate REFERENCE_DATE = LocalDate.of(2026, 8, 9);



    public PolicyRegister(
            PremiumCalculatorResolver premiumCalculatorResolver,
            PolicyRepository repository) {
        this.premiumCalculatorResolver = premiumCalculatorResolver;
        this.repository = repository;
    }

    public void add(Policy policy) {
        if (policy == null) {
            throw new InvalidPolicyDataException("UNKNOWN", "Policy must not be null");
        }

        if (repository.findByPolicyNumber(policy.getPolicyNumber()).isPresent()) {
            throw new DuplicatePolicyNumberException(
                    policy.getPolicyNumber(),
                    "A policy with number " + policy.getPolicyNumber() + " already exists"
            );
        }
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

        List<Policy> policyListGroupedByCustomer = repository.findByPolicyHolderFirstName(customerName);

        return policyListGroupedByCustomer;
    }

    public List<Policy> findByVehicleType(VehicleType vehicleType) {
        List<Policy> policyListGroupedByVehicleType = repository.findByVehicleType(vehicleType);

        return policyListGroupedByVehicleType;
    }

    public double   getPremium(String policyNumber) throws PolicyNotFoundException {
        Policy policy = repository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new PolicyNotFoundException(
                        policyNumber,
                        "No policy found with number: " + policyNumber
                ));
        return premiumCalculatorResolver.resolve(policy).calculatePremium(policy);
    }

    public List<Policy> findExpiringWithinDays(int days) {

        LocalDate endDate = REFERENCE_DATE.plusDays(days);
        List<Policy> policiesExpiringWithinDays = repository.findByExpiryDateBetween(REFERENCE_DATE, endDate);

        return policiesExpiringWithinDays;

    }

    public Map<VehicleType, Double> totalPremiumByVehicleType() {

        /*
            .sum() is a method provided by the Stream API in Java. It is used to calculate the sum of a stream of numeric values.
            The Collections objects in Java do not contain this method. Hence, we need to convert these collections objects to a Stream object
            specifically before we can operate on them. It is done using .stream() method

         */

        return repository.findAll()
                        .stream()
                                .collect(Collectors.groupingBy(
                                        Policy::getVehicleType,
                                        Collectors.summingDouble(
                                                policy -> premiumCalculatorResolver.resolve(policy).calculatePremium(policy)
                                        )
                                ));


    }

    public List<VehicleTypeCount> countPoliciesByVehicleType() {

        return repository.countPoliciesGroupedByVehicleType();
    }

    public Optional<String> getCustomerWithMostPolicies() {

        Pageable topOne = PageRequest.of(0, 1);

        List<String> customerNames = repository.findCustomerNamesOrderedByPolicyCount(topOne);

        return customerNames.isEmpty() ? Optional.empty() : Optional.of(customerNames.get(0));
    }

    public Map<String, Double> getTop5PoliciesByPremium() {
        List<Policy> policyMap = repository.findAll();

        return policyMap.stream()
                .map(policy -> Map.entry(policy.getPolicyNumber(), premiumCalculatorResolver.resolve(policy).calculatePremium(policy)))
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    public Policy getExistingPolicy(String policyNumber) throws PolicyNotFoundException {
        return repository.findByPolicyNumber(policyNumber).orElseThrow(
                () -> new PolicyNotFoundException(
                        policyNumber,
                        "No policy found with number: " + policyNumber
                )
        );
    }

    public void expire(String policyNumber) throws PolicyNotFoundException{
        Policy policy = getExistingPolicy(policyNumber);

        policy.expire();
    }

    public void renew(String policyNumber, LocalDate renewalDate) throws PolicyNotFoundException{
        Policy policy = getExistingPolicy(policyNumber);

        policy.renew(renewalDate);
    }

    public void recordClaim(String policyNumber) throws PolicyNotFoundException {
        Policy policy = getExistingPolicy(policyNumber);

        policy.recordClaim();
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
