package org.itiszakk.server.advice;

import org.itiszakk.server.exception.BalanceDTOException;
import org.itiszakk.server.exception.BalanceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultControllerAdvice {

    @ExceptionHandler(value = BalanceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBalanceNotFoundException(BalanceNotFoundException exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(value = BalanceDTOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBalanceNotFoundException(BalanceDTOException exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
