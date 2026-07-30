package com.wipfli.training.store;

import com.wipfli.training.model.PolicyHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicyHolderStore {
    private final Map<String, PolicyHolder> policyHolderMap = new HashMap<>();


    public void save(PolicyHolder policyHolder){
        policyHolderMap.put(policyHolder.getUserID(), policyHolder);
    }


    public PolicyHolder getPolicyHolderById(String policyHolderId){
        return policyHolderMap.get(policyHolderId);
    }

    public List<PolicyHolder> getAllPolicyHolders(){
        List<PolicyHolder> allPolicyHolders = new ArrayList<>();

        policyHolderMap.forEach((key, value) -> allPolicyHolders.add(value));

        return allPolicyHolders;
    }


}
