package com.att.tdp.popcorn_palace.Model;


import com.att.tdp.popcorn_palace.Util.Requests.ShowtimeRequest;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "showtimes")

@AllArgsConstructor
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double price;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;


    @Column(nullable = false)
    private String theater;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    public Showtime(ShowtimeRequest showtimeRequest, Movie movie){
        checkShowtimeParams(showtimeRequest);
        this.movie =movie;
        this.theater = showtimeRequest.getTheater();
        this.price = showtimeRequest.getPrice();
        this.startTime = showtimeRequest.getStartTime();
        this.endTime = showtimeRequest.getEndTime();
    }

    public Showtime() {
    }

    public void update(ShowtimeRequest showtimeRequest, Movie movie){
        checkShowtimeParams(showtimeRequest);
        this.movie = movie;
        this.theater = showtimeRequest.getTheater();
        this.price = showtimeRequest.getPrice();
        this.startTime = showtimeRequest.getStartTime();
        this.endTime = showtimeRequest.getEndTime();
    }

    private void checkShowtimeParams(ShowtimeRequest showtimeRequest){

        if (showtimeRequest.getEndTime().isBefore(showtimeRequest.getStartTime())){
            throw new IllegalArgumentException("Showtime must begin before it ends");
        }
        if (showtimeRequest.getPrice()<0){
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (showtimeRequest.getMovieId()<=0){
            throw new IllegalArgumentException("Showtime Movie ID must be negative");
        }
        if (showtimeRequest.getTheater()==null || showtimeRequest.getTheater().trim().isEmpty()){
            throw new IllegalArgumentException("Showtime theater name must be at least one character");
        }

    }


    public Long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public long getMovieId() {
        return movie.getId();
    }

    public String getTheater() {
        return theater;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

//

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setTheater(String theater) {
        this.theater = theater;
    }
}
