package com.brunoliveiradev.dscatalog.controllers.exceptions;

import com.brunoliveiradev.dscatalog.services.exceptions.DataBaseException;
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
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError errorBody = new StandardError();
        errorBody.setTimestamp(Instant.now());
        errorBody.setStatus(status.value());
        errorBody.setError("Resource not found");
        errorBody.setMessage(exception.getMessage());
        errorBody.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(errorBody);
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<StandardError> dataBase(DataBaseException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError errorBody = new StandardError();
        errorBody.setTimestamp(Instant.now());
        errorBody.setStatus(status.value());
        errorBody.setError("Database Integrity Violation Exception");
        errorBody.setMessage(exception.getMessage());
        errorBody.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(errorBody);
    }
}
