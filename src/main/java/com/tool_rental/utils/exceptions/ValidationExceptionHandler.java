package com.tool_rental.utils.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(
                getErrorsMap(ex.getBindingResult().getFieldErrors()),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<FieldError> errors) {
        var errorMessages = errors.stream()
                .map(fieldError -> "%s: %s".formatted(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        return Map.of("errors", errorMessages);

    }

}
