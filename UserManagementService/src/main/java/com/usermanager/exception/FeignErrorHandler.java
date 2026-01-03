package com.usermanager.exception;

import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FeignErrorHandler {

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<String> handleFeignException(FeignException ex) {

	    if (ex instanceof FeignException.NotFound) {
	        return ResponseEntity.status(404)
	                .body("User not found in Authentication Service");
	    }

	    if (ex instanceof FeignException.BadRequest) {
	        return ResponseEntity.status(400)
	                .body("Invalid request");
	    }

	    if (ex instanceof FeignException.Conflict) {
	        return ResponseEntity.status(409)
	                .body("Conflict — duplicate or invalid operation");
	    }

	    if (ex.status() == 500 && ex.getMessage().contains("Email already exists")) {
	        return ResponseEntity.status(409)
	                .body("User already exists");
	    }

	    return ResponseEntity.status(500)
	            .body("Authentication service error");
	}

}
