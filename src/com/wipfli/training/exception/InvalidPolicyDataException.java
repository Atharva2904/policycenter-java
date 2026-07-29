package com.wipfli.training.exception;

public class InvalidPolicyDataException extends PolicyBusinessException{

    public InvalidPolicyDataException(String policyNumber, String message){
        super(policyNumber, message);
    }

    public InvalidPolicyDataException(String policyNumber, String message, Throwable cause){
        super(policyNumber, message, cause);
    }
}
