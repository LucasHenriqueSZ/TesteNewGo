package com.lh.virtualDisk.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InavlidUserCreationException extends IllegalArgumentException {
    public InavlidUserCreationException(String message) {
        super(message);
    }
}
