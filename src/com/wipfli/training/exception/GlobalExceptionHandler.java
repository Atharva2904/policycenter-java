package com.wipfli.training.exception;

import com.wipfli.training.dto.ErrorResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PolicyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            PolicyNotFoundException exception
    )
    {
        ErrorResponse error = new ErrorResponse(
                exception.getPolicyNumber(),
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DuplicatePolicyNumberException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(
            DuplicatePolicyNumberException exception
    )
    {
        ErrorResponse error = new ErrorResponse(
                exception.getPolicyNumber(),
                exception.getMessage(),
                HttpStatus.CONFLICT.value()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(PolicyBusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            PolicyBusinessException exception
    )
    {
        ErrorResponse error = new ErrorResponse(
                exception.getPolicyNumber(),
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception exception
    )
    {
        ErrorResponse error = new ErrorResponse(
                "N/A",
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            org.springframework.dao.DataIntegrityViolationException exception
    ) {
        String message = "Data integrity violation: " + extractConstraintMessage(exception.getMessage());
        ErrorResponse error = new ErrorResponse("N/A", message, HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    private String extractConstraintMessage(String message) {
        if (message.contains("UNIQUE constraint")) {
            return "A record with this value already exists (unique constraint violation)";
        } else if (message.contains("FOREIGN KEY")) {
            return "Cannot perform operation due to related data";
        }
        return "Database constraint violation occurred";
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            org.springframework.web.bind.MethodArgumentNotValidException exception
    ) {
        String fieldErrors = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        String message = "Validation failed - " + fieldErrors;
        ErrorResponse error = new ErrorResponse("N/A", message, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(HttpMessageNotReadableException exception) {
        String message = exception.getMostSpecificCause() != null
                ? exception.getMostSpecificCause().getMessage()
                : exception.getMessage();

        String friendlyMessage;

        if (message != null && message.contains("not one of the values accepted for Enum class")) {
            if (message.contains("State")) {
                friendlyMessage = "Invalid holderState. Valid values are: IS, IN, MN";
            } else if (message.contains("VehicleType")) {
                friendlyMessage = "Invalid vehicleType. Valid values are: CAR, BIKE, TRUCK";
            } else {
                friendlyMessage = "Invalid enum value provided";
            }
        } else if (message != null && message.contains("JSON parse error")) {
            friendlyMessage = "Invalid JSON format in request body";
        } else {
            friendlyMessage = "Invalid request data or malformed JSON";
        }

        ErrorResponse error = new ErrorResponse("N/A", friendlyMessage, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(java.time.format.DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> handleDateTimeParseException(
            java.time.format.DateTimeParseException exception
    ) {
        String message = "Invalid date format: Expected dd/MM/yyyy format. Received: " + exception.getParsedString();
        ErrorResponse error = new ErrorResponse("N/A", message, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException exception
    ) {
        String paramName = exception.getName();
        String requiredType = exception.getRequiredType() != null
                ? exception.getRequiredType().getSimpleName()
                : "value";
        String value = exception.getValue() != null ? exception.getValue().toString() : "null";

        String message;
        if ("on".equals(paramName) && "LocalDate".equals(requiredType)) {
            message = "Invalid renewal date format. Expected dd/MM/yyyy. Received: " + value;
        } else if ("LocalDate".equals(requiredType)) {
            message = "Invalid date format for parameter '" + paramName + "'. Expected dd/MM/yyyy. Received: " + value;
        } else {
            message = "Invalid value for parameter '" + paramName + "'. Expected " + requiredType + " but got '" + value + "'";
        }

        ErrorResponse error = new ErrorResponse("N/A", message, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    private @NonNull String getString(String paramName, String requiredType, String value) {
        String message;

        if ("on".equals(paramName) && "LocalDate".equals(requiredType)) {
            message = "Invalid renewal date format. Expected dd/MM/yyyy format. Received: " + value;
        } else if ("LocalDate".equals(requiredType)) {
            message = "Invalid date format for parameter '" + paramName + "': Expected dd/MM/yyyy format. Received: " + value;
        } else {
            message = "Invalid value for parameter '" + paramName + "': Expected " + requiredType + " but got '" + value + "'";
        }
        return message;
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex) {
        String message = "Missing required parameter: " + ex.getParameterName();
        return ResponseEntity.badRequest().body(
                new ErrorResponse("N/A", message, HttpStatus.BAD_REQUEST.value())
        );
    }


}
