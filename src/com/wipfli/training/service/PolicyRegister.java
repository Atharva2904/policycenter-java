package com.wipfli.training.service;

import com.wipfli.training.exception.DuplicatePolicyNumberException;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.model.Policy;

import java.util.HashMap;

public class PolicyRegister {
    private final HashMap<String, Policy> policyHashMap;

    public PolicyRegister() {
        policyHashMap = new HashMap<>();
    }

    public void add(Policy policy) {
        String policyNumber = policy.getPolicyNumber();
        if (policyHashMap.containsKey(policyNumber)) {
            throw new DuplicatePolicyNumberException(policyNumber,
                    String.format("DUPLICATE POLICY ERROR: Policy with number %s already exists!!", policyNumber));
        }
        policyHashMap.put(policy.getPolicyNumber(), policy);
    }

    public Policy findByNumber(String policyNumber) throws PolicyNotFoundException {

        Policy policy = policyHashMap.get(policyNumber);

        if (policy == null) {
            throw new PolicyNotFoundException(
                    String.format("Policy with number %s not found!", policyNumber)
            );
        }

        return policy;

    }
}
