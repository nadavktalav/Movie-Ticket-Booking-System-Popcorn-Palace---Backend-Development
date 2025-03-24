package com.att.tdp.popcorn_palace.Service;

import com.att.tdp.popcorn_palace.Model.Movie;
import com.att.tdp.popcorn_palace.Model.Showtime;
import com.att.tdp.popcorn_palace.Repository.MovieRepository;
import com.att.tdp.popcorn_palace.Repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.Util.Exceptions.InternalServerException;
import com.att.tdp.popcorn_palace.Util.Exceptions.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.Util.Requests.ShowtimeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public ShowtimeService(ShowtimeRepository showtimeRepository, MovieRepository movieRepository) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
    }

    public Showtime getShowtime(Long showtimeId) {
        try {
            Optional<Showtime> optionalShowtime = showtimeRepository.findById(showtimeId);
            if (optionalShowtime.isEmpty()){
                throw new ResourceNotFoundException("Showtime with id "+ showtimeId + " was not found" );
            }
            Showtime showtime = optionalShowtime.get();
            return showtime;
        }
        catch (Exception e){
            throw e;
        }
    }

    // Update an existing showtime
    public Showtime updateShowtime(Long showtimeId, ShowtimeRequest showtimeRequest) {
        Optional<Showtime> optionalShowtime = showtimeRepository.findById(showtimeId);
        if (optionalShowtime.isEmpty()){
            throw new ResourceNotFoundException("showtime with id "+ showtimeId + " was not founded");
        }
        Showtime showtime = optionalShowtime.get();
        Optional<Movie> optionalMovie = movieRepository.findById(showtimeRequest.getMovieId());
        if (optionalMovie.isEmpty()){
            throw new ResourceNotFoundException("Movie with id "+ showtimeRequest.getMovieId() + " was not found");
        }
        List<Showtime> overlappingShows = showtimeRepository.findShowtimesByTimeRangeAndTheater(showtimeRequest.getStartTime(), showtimeRequest.getEndTime(), showtimeRequest.getTheater());
        if (!overlappingShows.isEmpty()) {
            if (overlappingShows.size() != 1 || !overlappingShows.getFirst().getId().equals(showtimeId)) {
                throw new InternalServerException("Showtime overlaps with existing showtime");
            }
        }
        showtime.update(showtimeRequest, optionalMovie.get());
        return showtimeRepository.save(showtime);
    }

    public Showtime addShowtime(ShowtimeRequest showtimeRequest) {
        List<Showtime> overlappingShows = showtimeRepository.findShowtimesByTimeRangeAndTheater(showtimeRequest.getStartTime(), showtimeRequest.getEndTime(), showtimeRequest.getTheater());
        if (!overlappingShows.isEmpty()) {
            throw new InternalServerException("Showtime overlaps with existing showtime");
        }
        Optional<Movie> optionalMovie = movieRepository.findById(showtimeRequest.getMovieId());
        if (optionalMovie.isEmpty()){
            throw new ResourceNotFoundException("Movie with id "+ showtimeRequest.getMovieId() + " was not found");
        }
        Showtime showtime = new Showtime(showtimeRequest, optionalMovie.get());
        return showtimeRepository.save(showtime);
    }

    public void deleteShowtime(long showtimeId) {
        try{
            showtimeRepository.delete(getShowtime(showtimeId));
        }
        catch (Exception e){
            throw new InternalServerException("Internal Error while try to delete showtime with id: " + showtimeId);
        }
    }


}

