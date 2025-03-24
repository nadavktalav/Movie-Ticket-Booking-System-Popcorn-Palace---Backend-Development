package com.att.tdp.popcorn_palace.Util.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Setter
public class ShowtimeRequest {
    long movieId;
    double price ;
    String theater;
    LocalDateTime startTime;
    LocalDateTime endTime;

    public ShowtimeRequest(long movieId, double price, String theater, LocalDateTime startTime, LocalDateTime endTime){
        this.movieId = movieId;
        this.price = price;
        this.theater = theater;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getTheater() {
        return theater;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public long getMovieId() {
        return movieId;
    }

    public double getPrice() {
        return price;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setTheater(String theater) {
        this.theater = theater;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
