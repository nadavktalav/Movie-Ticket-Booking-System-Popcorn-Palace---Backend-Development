package com.att.tdp.popcorn_palace.Util.Exceptions;

// If there's an issue with database or server errors
public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }
}
