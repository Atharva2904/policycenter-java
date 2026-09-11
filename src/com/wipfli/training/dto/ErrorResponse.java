package com.wipfli.training.dto;

import java.sql.Timestamp;

public record ErrorResponse(
        String policyNumber,
        String message,
        Timestamp timestamp
) {}