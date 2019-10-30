package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class CoreException extends Exception {

    private HttpStatus httpStatus;

    public CoreException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public CoreException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public CoreException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
