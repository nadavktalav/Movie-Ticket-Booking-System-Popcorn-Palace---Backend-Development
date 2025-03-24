package com.att.tdp.popcorn_palace.Controller;

import com.att.tdp.popcorn_palace.Model.Movie;
import com.att.tdp.popcorn_palace.Service.MovieService;
import com.att.tdp.popcorn_palace.Util.Exceptions.InternalServerException;
import com.att.tdp.popcorn_palace.Util.Exceptions.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.Util.Requests.BookingRequest;
import com.att.tdp.popcorn_palace.Util.Requests.MovieRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    public MovieController(MovieService movieService){
        this.movieService=movieService;
    }

    private MovieService movieService;

    @GetMapping("all")
    public ResponseEntity<?> getAllMovies() {
        try {
            List<Movie> movies = movieService.getAllMovies();
            return ResponseEntity.ok(movies);
        }
        catch (InternalServerException e){
            return new ResponseEntity<String>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (ResourceNotFoundException e){
            return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @PostMapping()
    public ResponseEntity<?> addMovie(@RequestBody MovieRequest movieRequest) {
        try {

            Movie movie = movieService.addMovie(movieRequest);
            return ResponseEntity.ok(movie);
        } catch (InternalServerException e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @PostMapping("update/{movieTitle}")
    public ResponseEntity<?> updateMovie(@PathVariable String movieTitle , @RequestBody MovieRequest movieRequest) {
        try {
            movieService.updateMovie(movieTitle, movieRequest);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (InternalServerException e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @DeleteMapping("/{movieTitle}")
    public ResponseEntity<String> deleteMovie(@PathVariable String movieTitle) {
        try {
            movieService.deleteMovie(movieTitle);
            return new ResponseEntity<String>( "", HttpStatus.OK);
        } catch (InternalServerException e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity( "An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
