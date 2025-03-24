package com.att.tdp.popcorn_palace.Util.Exceptions;

// If a requested resource (like Movie, Showtime, Booking) is not found
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
