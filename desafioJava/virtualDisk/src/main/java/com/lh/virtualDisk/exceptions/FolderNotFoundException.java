package com.lh.virtualDisk.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FolderNotFoundException extends IllegalArgumentException {
    public FolderNotFoundException(String message) {
        super(message);
    }
}
