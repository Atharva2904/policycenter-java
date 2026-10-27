package com.wipfli.training.dto;

public record APIStatus(
        int statusCode,
        boolean success,
        String message
) {
    public static APIStatus success(int statusCode, String message) {
        return new APIStatus(statusCode, true, message);
    }

    public static APIStatus success(String message) {
        return new APIStatus(200, true, message);
    }

    public static APIStatus failure(int statusCode, String message) {
        return new APIStatus(statusCode, false, message);
    }

    public static APIStatus failure(String message) {
        return new APIStatus(500, false, message);
    }
}
