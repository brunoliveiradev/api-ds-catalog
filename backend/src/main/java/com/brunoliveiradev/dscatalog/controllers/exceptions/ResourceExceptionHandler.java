package com.brunoliveiradev.dscatalog.controllers.exceptions;

import com.brunoliveiradev.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler  {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
        StandardError errorBody = new StandardError();
        errorBody.setTimestamp(Instant.now());
        errorBody.setStatus(HttpStatus.NOT_FOUND.value());
        errorBody.setError("Resource not found");
        errorBody.setMessage(exception.getMessage());
        errorBody.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }
}
