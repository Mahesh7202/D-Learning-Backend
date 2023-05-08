package com.elearning.studentservice.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

import static com.elearning.studentservice.model.Enum.Category.API_ERROR;
import static com.elearning.studentservice.model.Enum.Category.INVALID_REQUEST_ERROR;
import static com.elearning.studentservice.model.Enum.Code.BAD_REQUEST;
import static com.elearning.studentservice.model.Enum.Code.NOT_FOUND;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Error error = new Error(
                API_ERROR,
                BAD_REQUEST,
                ex.getMessage(),
                new Date()
        );
         return new ResponseEntity<Object>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);

    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Error error = new Error(
                INVALID_REQUEST_ERROR,
                NOT_FOUND,
                ex.getMessage(),
                new Date()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        Error error = new Error(
                API_ERROR,
                BAD_REQUEST,
                ex.getMessage(),
                new Date()
        );

        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UserNotCreated.class,RestException.class})
    public ResponseEntity<Object> handleStudentNotCreated(Exception exception){
        Error error = new Error(
                API_ERROR,
                BAD_REQUEST,
                exception.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UserNotFound.class})
    public ResponseEntity<Object> handleStudentNotFound(Exception exception){
        Error error = new Error(
                API_ERROR,
                NOT_FOUND,
                exception.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }



}
