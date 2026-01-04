package com.hospitalmanager.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound(String msg){
        super(msg);
    }
}