package com.wipfli.training.exception;

public abstract class PolicyBusinessException extends RuntimeException{
    private String policyNumber;

    public PolicyBusinessException(String policyNumber, String message){
        super(message);
        this.policyNumber = policyNumber;
    }

    public PolicyBusinessException(String policyNumber, String message, Throwable cause){
        super(message, cause);
        this.policyNumber = policyNumber;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }
}
