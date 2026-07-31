package com.wipfli.training.exception;

public class PolicyNotFoundException extends Exception{

    private final String policyNumber;

    public PolicyNotFoundException(String policyNumber, String message) {
        super(message);
        this.policyNumber = policyNumber;
    }


    public String getPolicyNumber() {
        return policyNumber;
    }
}
