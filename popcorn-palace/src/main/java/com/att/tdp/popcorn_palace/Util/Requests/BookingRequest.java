package com.att.tdp.popcorn_palace.Util.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
public class BookingRequest {
    long showtimeId;
    int seatNumber;
    String userId;

    public int getSeatNumber() {
        return seatNumber;
    }

    public String getUserId() {
        return userId;
    }

    public long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}
