package com.wipfli.training.service;

import com.wipfli.training.exception.DuplicatePolicyNumberException;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.model.Policy;

import java.util.Hashtable;

public class PolicyRegister {
    private final Hashtable<String, Policy> policyHashtable;

    public PolicyRegister() {
        policyHashtable = new Hashtable<>();
    }

    public void add(Policy policy) {
        String policyNumber = policy.getPolicyNumber();
        if (policyHashtable.containsKey(policyNumber)) {
            throw new DuplicatePolicyNumberException(policyNumber,
                    String.format("DUPLICATE POLICY ERROR: Policy with number %s already exists!!", policyNumber));
        }
        policyHashtable.put(policy.getPolicyNumber(), policy);
    }

    public Policy findByNumber(String policyNumber) throws PolicyNotFoundException{

        Policy policy = policyHashtable.get(policyNumber);

        if (policy == null) {
            throw new PolicyNotFoundException(
                    policyNumber,
                    String.format("Policy with number %s not found!", policyNumber)
            );
        }

        return policy;

    }
}
