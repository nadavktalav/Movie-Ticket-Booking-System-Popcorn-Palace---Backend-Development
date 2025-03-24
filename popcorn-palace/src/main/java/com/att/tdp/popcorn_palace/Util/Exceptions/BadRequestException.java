package com.att.tdp.popcorn_palace.Util.Exceptions;

// If a request is invalid (e.g., trying to book an unavailable seat)
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
