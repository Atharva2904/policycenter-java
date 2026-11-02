package com.wipfli.training.dto;

public record ErrorResponse(
        String policyNumber,
        String message,
        APIStatus apiStatus
) {
    public ErrorResponse(String policyNumber, String message, int statusCode) {
        this(policyNumber, message, APIStatus.failure(statusCode, message));
    }

    public ErrorResponse(String policyNumber, String message) {
        this(policyNumber, message, 500);
    }
}
