package com.att.tdp.popcorn_palace.Model;

import com.att.tdp.popcorn_palace.Util.Requests.MovieRequest;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "movies")
@AllArgsConstructor
public class Movie {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private double rating;

    @Column(nullable = false)
    private int releaseYear;


    public Movie() {
    }

    public double getRating() {
        return rating;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public Long getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    public Movie (MovieRequest movieRequest){
        if (movieRequest.getDuration() <=0){
            throw new IllegalArgumentException("movie duration must be positive number");
        }
        if (movieRequest.getRating()<0 ){
            throw new IllegalArgumentException("movie rating cannot be negative");
        }
        if (movieRequest.getGenre() == null || movieRequest.getGenre().trim().isEmpty()){
            throw new IllegalArgumentException("movie must contain a genre");
        }
        if (movieRequest.getTitle() == null ||movieRequest.getTitle().trim().isEmpty()){
            throw new IllegalArgumentException("movie must contain a title with at least one character");
        }

        this.duration = movieRequest.getDuration();
        this.rating = movieRequest.getRating();
        this.genre = movieRequest.getGenre();
        this.title = movieRequest.getTitle();
        this.releaseYear = movieRequest.getReleaseYear();

    }
}

