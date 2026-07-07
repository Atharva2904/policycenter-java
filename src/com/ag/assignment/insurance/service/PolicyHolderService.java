package com.ag.assignment.insurance.service;

import com.ag.assignment.insurance.model.PolicyHolder;
import com.ag.assignment.insurance.repository.PolicyHolderRepository;

public class PolicyHolderService {

    private final PolicyHolderRepository policyHolderRepository;

    public PolicyHolderService(PolicyHolderRepository policyHolderRepository){
        this.policyHolderRepository = policyHolderRepository;
    }

    public PolicyHolder getPolicyHolder(String policyHolderId){
        return policyHolderRepository.getPolicyHolderById(policyHolderId);
    }

    public void registerPolicyHolder(PolicyHolder policyHolder){
        policyHolderRepository.save(policyHolder);
    }



}
