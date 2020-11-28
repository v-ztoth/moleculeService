package com.zoltan.toth.molecule.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CalculateApiException extends RuntimeException{
    public CalculateApiException(String message) {
        super(message);
    }
}
