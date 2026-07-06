package com.ag.assignment.insurance.repository;

import com.ag.assignment.insurance.model.Policy;

import java.math.BigInteger;
import java.util.*;

public class PolicyRepository {
    private final Map<BigInteger, Policy> policyMap = new HashMap<>();

    public void save(Policy policy){
        policyMap.put(policy.getPolicyNumber(), policy);
    }

    public Policy getPolicyByNumber(BigInteger policyNumber){
        return policyMap.get(policyNumber);
    }

    public Set<Policy> findDuplicatePolicies(List<Policy> policyList){
        Set<Policy> uniquePolicies = new HashSet<>();
        Set<Policy> duplicates = new HashSet<>();
        for(Policy policy: policyList){
            if(!uniquePolicies.add(policy)){
                duplicates.add(policy);
            }
        }

        return duplicates;
    }
}
