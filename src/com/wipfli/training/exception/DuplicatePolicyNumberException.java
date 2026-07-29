package com.wipfli.training.exception;

public class DuplicatePolicyNumberException extends PolicyBusinessException{

    public DuplicatePolicyNumberException(String policyNumber, String message){
        super(policyNumber, message);
    }

    public DuplicatePolicyNumberException(String policyNumber, String message, Throwable cause){
        super(policyNumber, message, cause);
    }

}
