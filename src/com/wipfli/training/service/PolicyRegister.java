package com.wipfli.training.service;

import com.wipfli.training.exception.DuplicatePolicyNumberException;
import com.wipfli.training.exception.InvalidPolicyDataException;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.model.Policy;
import com.wipfli.training.model.PremiumCalculable;
import com.wipfli.training.model.VehicleType;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class PolicyRegister {
    private final Map<String, Policy> policyHashtable;
    private final Map<String, List<Policy>> policiesByCustomerName;
    private final EnumMap<VehicleType, List<Policy>> policiesByVehicleType;
    private final NavigableMap<LocalDate, List<Policy>> policiesByExpiryDate;
    private final LocalDate REFERENCE_DATE = LocalDate.of(2026, 8, 9);
    private PremiumCalculable premiumCalculator;



    public PolicyRegister(PremiumCalculable premiumCalculator) {
        policyHashtable = new Hashtable<>();
        policiesByCustomerName = new Hashtable<>();
        policiesByVehicleType = new EnumMap<>(VehicleType.class);
        policiesByExpiryDate = new TreeMap<>();
        this.premiumCalculator = premiumCalculator;
    }

    public void add(Policy policy) {
        if (policy == null) {
            throw new InvalidPolicyDataException("NULL_POLICY",
                    "Policy object cannot be null!!");
        }

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

    public Optional<Policy> findByNumber(String policyNumber) {
        return Optional.ofNullable(policyHashtable.get(policyNumber));

    }

    public List<Policy> findByCustomer(String customerName) {
        /*
            Optional.ofNullable() method is used to handle the null values gracefully without the risk of encountering a NullPointerException.
            If .get() returns a null value, the Optional.ofNullable() wraps it inside an empty Optional Object
            The orElse() detects that Optional is Empty and replaces the empty value with an immutable Empty List
         */

        List<Policy> policyListGroupedByCustomer = Optional.ofNullable(policiesByCustomerName.get(customerName))
                .orElse(Collections.emptyList());

        return policyListGroupedByCustomer;
    }

    public List<Policy> findByVehicleType(VehicleType vehicleType) {
        List<Policy> policyListGroupedByVehicleType = Optional.ofNullable(policiesByVehicleType.get(vehicleType))
                .orElse(Collections.emptyList());

        return policyListGroupedByVehicleType;
    }

    public List<Policy> findExpiringWithinDays(int days) {
        LocalDate targetDate = this.REFERENCE_DATE.plusDays(days);

        List<Policy> policiesExpiringWithinDays = new ArrayList<>();

        // Since we're using a TreeMap for storing the List<Policy> by expiry dates, we can efficiently retrieve the policies that are expiring within the specified range of dates using the subMap method.
        // This method returns a view of the portion of this map whose keys range from currentDate (inclusive) to targetDate (inclusive).
        // We then iterate over the values of this subMap and add all the policies to our result list.


        policiesByExpiryDate.subMap(REFERENCE_DATE, false, targetDate, true)
                .values()
                .forEach(policiesExpiringWithinDays::addAll);


        return policiesExpiringWithinDays;

    }

    public Hashtable<VehicleType, Double> totalPremiumByVehicleType() {
        Hashtable<VehicleType, Double> totalPremiumGroupedByVehicleType = new Hashtable<>();

        /*
            .sum() is a method provided by the Stream API in Java. It is used to calculate the sum of a stream of numeric values.
            The Collections objects in Java do not contain this method. Hence, we need to convert these collections objects to a Stream object
            specifically before we can operate on them. It is done using .stream() method

         */
        policiesByVehicleType.forEach((vehicleType, policies) ->
                totalPremiumGroupedByVehicleType.put(vehicleType, policies.stream()
                        .mapToDouble(p -> premiumCalculator.calculatePremium(p))
                        .sum()
                )
        );


        return totalPremiumGroupedByVehicleType;
    }

    public Hashtable<VehicleType, Integer> countPoliciesByVehicleType() {
        Hashtable<VehicleType, Integer> countOfPoliciesGroupedByVehicleType = new Hashtable<>();

        policiesByVehicleType.forEach(((vehicleType, policies) -> countOfPoliciesGroupedByVehicleType.put(vehicleType, policies.size())));
        return countOfPoliciesGroupedByVehicleType;
    }

    public Optional<String> getCustomerWithMostPolicies() {

        return policiesByCustomerName.entrySet().stream()
                .max(Comparator.comparingInt(entry -> entry.getValue().size()))
                .map(Map.Entry::getKey);
    }

    public Map<String, Double> getTop5PoliciesByPremium() {


        return policyHashtable.values().stream()
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

    public List<String> getCustomersWithMatchingPattern(String customerName) {
        List<String> matchingCustomerNames = new ArrayList<>();
        List<Policy> policyListForGivenCustomer = findByCustomer(customerName);


        Map<Class<? extends Policy>, Long> targetPattern = policyListForGivenCustomer.stream()
                .collect(Collectors.groupingBy(
                        Policy::getClass,
                        Collectors.counting()
                ));


        policiesByCustomerName.forEach((customer, policies) -> {
            Map<Class<? extends Policy>, Long> currentPattern = policies.stream()
                    .collect(Collectors.groupingBy(
                            Policy::getClass,
                            Collectors.counting()
                    ));

            if (currentPattern.equals(targetPattern)) matchingCustomerNames.add(customer);

        });


        return matchingCustomerNames;
    }


}
