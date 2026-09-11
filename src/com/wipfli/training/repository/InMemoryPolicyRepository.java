package com.wipfli.training.repository;

import com.wipfli.training.exception.DuplicatePolicyNumberException;
import com.wipfli.training.exception.InvalidPolicyDataException;
import com.wipfli.training.model.Policy;
import com.wipfli.training.model.VehicleType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;


@Repository
public class InMemoryPolicyRepository implements PolicyRepository{
    private final Map<String, Policy> policyMap;
    private final Map<String, List<Policy>> policiesByCustomerName;
    private final EnumMap<VehicleType, List<Policy>> policiesByVehicleType;
    private final NavigableMap<LocalDate, List<Policy>> policiesByExpiryDate;
    private final LocalDate REFERENCE_DATE = LocalDate.of(2026, 8, 9);


    public InMemoryPolicyRepository(){
        this.policyMap = new Hashtable<>();
        this.policiesByCustomerName = new Hashtable<>();
        this.policiesByVehicleType = new EnumMap<>(VehicleType.class);
        this.policiesByExpiryDate = new TreeMap<>();
    }

    @Override
    public void save(Policy policy) {
        if (policy == null) {
            throw new InvalidPolicyDataException(
                    "NULL_POLICY",
                    "Policy object cannot be null!"
            );
        }

        String policyNumber = policy.getPolicyNumber();
        String customerName = policy.getPolicyHolder().getDisplayName();
        VehicleType vehicleType = policy.getVehicleType();
        LocalDate policyExpiryDate = policy.getExpiryDate();

        if (policyMap.containsKey(policyNumber)) {
            throw new DuplicatePolicyNumberException(
                    policyNumber,
                    String.format(
                            "DUPLICATE POLICY ERROR: Policy with number %s already exists!",
                            policyNumber
                    )
            );
        }

        policyMap.put(policyNumber, policy);

        policiesByCustomerName
                .computeIfAbsent(customerName, key -> new ArrayList<>())
                .add(policy);

        policiesByVehicleType
                .computeIfAbsent(vehicleType, key -> new ArrayList<>())
                .add(policy);

        policiesByExpiryDate
                .computeIfAbsent(policyExpiryDate, key -> new ArrayList<>())
                .add(policy);
    }

    @Override
    public Optional<Policy> findByPolicyNumber(String policyNumber) {
        return Optional.ofNullable(policyMap.get(policyNumber));
    }

    @Override
    public List<Policy> findByCustomerName(String customerName) {
        return policiesByCustomerName.getOrDefault(customerName, Collections.emptyList());
    }

    @Override
    public List<Policy> findByVehicleType(VehicleType vehicleType) {
        List<Policy> policyListGroupedByVehicleType = Optional.ofNullable(policiesByVehicleType.get(vehicleType))
                .orElse(Collections.emptyList());

        return policyListGroupedByVehicleType;
    }

    @Override
    public List<Policy> findByExpiryDateRange(int days) {
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

    @Override
    public Hashtable<VehicleType, Integer> countPoliciesByVehicleType() {
        Hashtable<VehicleType, Integer> countOfPoliciesGroupedByVehicleType = new Hashtable<>();

        policiesByVehicleType.forEach(((vehicleType, policies) -> countOfPoliciesGroupedByVehicleType.put(vehicleType, policies.size())));
        return countOfPoliciesGroupedByVehicleType;
    }

    @Override
    public Optional<String> getCustomerWithMostPolicies() {
        return policiesByCustomerName.entrySet().stream()
                .max(Comparator.comparingInt(entry -> entry.getValue().size()))
                .map(Map.Entry::getKey);
    }


    @Override
    public Map<String, Policy> findAll() {
        return new HashMap<>(policyMap);
    }
}
