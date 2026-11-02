package com.wipfli.training.dto;

import java.time.LocalDateTime;

public record APIStatus(
        int statusCode,
        boolean success,
        String message,
        LocalDateTime timestamp
) {
    public static APIStatus success(int statusCode, String message) {
        return new APIStatus(statusCode, true, message, LocalDateTime.now());
    }

    public static APIStatus success(String message) {
        return new APIStatus(200, true, message, LocalDateTime.now());
    }

    public static APIStatus failure(int statusCode, String message) {
        return new APIStatus(statusCode, false, message, LocalDateTime.now());
    }

    public static APIStatus failure(String message) {
        return new APIStatus(500, false, message, LocalDateTime.now());
    }

    // Convenience methods for common status codes
    public static APIStatus created(String message) {
        return new APIStatus(201, true, message, LocalDateTime.now());
    }

    public static APIStatus ok(String message) {
        return new APIStatus(200, true, message, LocalDateTime.now());
    }

    public static APIStatus badRequest(String message) {
        return new APIStatus(400, false, message, LocalDateTime.now());
    }

    public static APIStatus notFound(String message) {
        return new APIStatus(404, false, message, LocalDateTime.now());
    }

    public static APIStatus conflict(String message) {
        return new APIStatus(409, false, message, LocalDateTime.now());
    }
}
