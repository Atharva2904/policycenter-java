package com.wipfli.training.exception;

public class IllegalStatusChangeException extends PolicyBusinessException{

    public IllegalStatusChangeException(String policyNumber, String message){
        super(policyNumber, message);
    }

    public IllegalStatusChangeException(String policyNumber, String message, Throwable cause){
        super(policyNumber, message, cause);
    }
}
