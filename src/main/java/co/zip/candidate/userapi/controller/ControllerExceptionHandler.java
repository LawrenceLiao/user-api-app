package co.zip.candidate.userapi.controller;


import co.zip.candidate.userapi.dto.error.ErrorDto;
import co.zip.candidate.userapi.exception.DuplicateElementsException;
import co.zip.candidate.userapi.exception.InsufficientMonthlySurplusException;
import co.zip.candidate.userapi.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DuplicateElementsException.class)
    @ResponseStatus(value = CONFLICT)
    public ErrorDto handleDuplicateElementsException(DuplicateElementsException ex) {
        return ErrorDto.builder()
                .message("DUPLICATE_ELEMENTS")
                .details(ex.getMessage())
                .build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = NOT_FOUND)
    public ErrorDto handleUserNotFoundException(UserNotFoundException ex) {
        return ErrorDto.builder()
                .message("USER_NOT_FOUND")
                .details(ex.getMessage())
                .build();
    }

    @ExceptionHandler(InsufficientMonthlySurplusException.class)
    @ResponseStatus(value = PRECONDITION_FAILED)
    public ErrorDto handleInsufficientMonthlySurplusException(InsufficientMonthlySurplusException ex) {
        return ErrorDto.builder()
                .message("INSUFFICIENT_SURPLUS")
                .details(ex.getMessage())
                .build();
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus
    public ErrorDto handleValidationException(BindException ex) {
        log.info("Input argument is invalid", ex);
        return ErrorDto.builder()
                .message("INVALID_ARGUMENT")
                .details(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = SERVICE_UNAVAILABLE)
    public ErrorDto handleException(Exception ex) {
        log.error("There is an exception occurred", ex);
        return ErrorDto.builder()
                .message("ERROR_OCCURRED_IN_SERVER_SIDE")
                .details("Please contact admin")
                .build();
    }
}
