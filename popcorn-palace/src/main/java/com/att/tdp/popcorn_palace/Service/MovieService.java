package com.att.tdp.popcorn_palace.Service;

import com.att.tdp.popcorn_palace.Model.Movie;
import com.att.tdp.popcorn_palace.Repository.MovieRepository;
import com.att.tdp.popcorn_palace.Util.Exceptions.InternalServerException;
import com.att.tdp.popcorn_palace.Util.Exceptions.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.Util.Requests.MovieRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public MovieRepository getMovieRepository() {
        return movieRepository;
    }

    public List<Movie> getAllMovies() {
        try {
            return movieRepository.findAll();
        }
        catch(Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    public Movie getMovieByTitle(String title) {
        try {
            Optional<Movie> optionalMovie= movieRepository.findMovieByTitle(title);
            // check if movie exist
            if (optionalMovie.isEmpty()){
                throw new ResourceNotFoundException("Movie not found");
            }
            else{
                return optionalMovie.get();
            }
        }
        catch (ResourceNotFoundException e){
            throw new InternalServerException(e.getMessage());
        }
        catch(Exception e){
            throw new InternalServerException("Internal Error while getting movie with title: " + title);
        }
    }

    public Movie addMovie(MovieRequest movieRequest) {
        try {
            Movie movie = new Movie(movieRequest);
            // check if movie already exist
            if (movieRepository.findMovieByTitle(movieRequest.getTitle()).isPresent()){
                throw new InternalServerException("Movie with title: " + movie.getTitle() + " already exists");
            }
            return movieRepository.save(movie);
        }
        catch (InternalServerException | ResourceNotFoundException e){
            throw e;
        }
        catch (Exception e){
            throw new InternalServerException("Internal Error while adding movie");
        }
    }

    public Movie updateMovie(String movieTitle, MovieRequest movieRequest) throws InternalServerException{
        try {
            // update each movie field
            if (movieRepository.findMovieByTitle(movieRequest.getTitle()).isPresent()){
                throw new InternalServerException("Movie with title: " + movieRequest.getTitle() + " already exists");
            }
            Movie movie = getMovieByTitle(movieTitle);
            movie.setDuration(movieRequest.getDuration());
            movie.setGenre(movieRequest.getGenre());
            movie.setRating(movieRequest.getRating());
            movie.setReleaseYear(movieRequest.getReleaseYear());
            movie.setTitle(movieRequest.getTitle());
            // error if title not exist
            return movieRepository.save(movie);
        }
        catch (InternalServerException e){
            throw e;
        }
        catch (Exception e){
            throw new InternalServerException("Internal Error while adding movie");
        }
    }

    public void deleteMovie(String movieTitle) {
        try{
            movieRepository.delete(getMovieByTitle(movieTitle));
        }
        catch (Exception e){
            throw new InternalServerException("Internal Error while try to delete movie with title: " + movieTitle);
        }
    }
}
