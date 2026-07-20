package com.wipfli.training.service;

import com.wipfli.training.model.PolicyHolder;
import com.wipfli.training.store.PolicyHolderStore;

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
