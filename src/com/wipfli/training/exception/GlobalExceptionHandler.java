package com.wipfli.training.exception;

import com.wipfli.training.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.time.Instant;

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
                Timestamp.from(Instant.now())
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
                Timestamp.from(Instant.now())
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
                Timestamp.from(Instant.now())
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
                Timestamp.from(Instant.now())
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
