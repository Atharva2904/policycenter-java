package com.ag.assignment.insurance.service;

import com.ag.assignment.insurance.model.PolicyHolder;
import com.ag.assignment.insurance.repository.PolicyHolderStore;

public class PolicyHolderService {

    private final PolicyHolderStore policyHolderStore;

    public PolicyHolderService(PolicyHolderStore policyHolderStore){
        this.policyHolderStore = policyHolderStore;
    }

    public PolicyHolder getPolicyHolder(String policyHolderId){
        return policyHolderStore.getPolicyHolderById(policyHolderId);
    }

    public void registerPolicyHolder(PolicyHolder policyHolder){
        policyHolderStore.save(policyHolder);
    }



}
