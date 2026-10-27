package com.wipfli.training.dto;

import java.sql.Timestamp;

public record ErrorResponse(
        String policyNumber,
        String message,
        Timestamp timestamp,
        APIStatus apiStatus
) {
    public ErrorResponse(String policyNumber, String message, Timestamp timestamp) {
        this(policyNumber, message, timestamp, APIStatus.failure(500, message));
    }
}
