package com.att.tdp.popcorn_palace.UnitTest.Service;

import com.att.tdp.popcorn_palace.Model.Movie;
import com.att.tdp.popcorn_palace.Repository.MovieRepository;
import com.att.tdp.popcorn_palace.Service.MovieService;
import com.att.tdp.popcorn_palace.Util.Exceptions.InternalServerException;
import com.att.tdp.popcorn_palace.Util.Requests.MovieRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    private MovieService movieService;

    private Movie sampleMovie;
    private MovieRequest sampleMovieRequest;

    @BeforeEach
    void setUp() {
        movieService = new MovieService(movieRepository);

        sampleMovie = new Movie();
        sampleMovie.setId(1L);
        sampleMovie.setTitle("Test Movie");
        sampleMovie.setGenre("Action");
        sampleMovie.setDuration(120);
        sampleMovie.setReleaseYear(2023);
        sampleMovie.setRating(4.5);

        sampleMovieRequest = new MovieRequest( "Test Movie", "action", 120, 4.5,2023);

    }

    @Test
    void getAllMovies_success() {

        List<Movie> movieList = Arrays.asList(sampleMovie);
        when(movieRepository.findAll()).thenReturn(movieList);

        List<Movie> result = movieService.getAllMovies();

        assertEquals(movieList.size(), result.size());
        assertEquals(movieList.get(0).getTitle(), result.get(0).getTitle());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void getAllMovies_WhenExceptionOccurs() {

        when(movieRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        InternalServerException exception = assertThrows(InternalServerException.class, () -> {
            movieService.getAllMovies();
        });
        assertEquals("Database error", exception.getMessage());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void getMovieByTitle_WhenMovieExists() {

        when(movieRepository.findMovieByTitle("Test Movie")).thenReturn(Optional.of(sampleMovie));

        Movie result = movieService.getMovieByTitle("Test Movie");

        assertNotNull(result);
        assertEquals(sampleMovie.getTitle(), result.getTitle());
        verify(movieRepository, times(1)).findMovieByTitle("Test Movie");
    }

    @Test
    void getMovieByTitle_WhenMovieDoesNotExist() {
        when(movieRepository.findMovieByTitle("Nonexistent Movie")).thenReturn(Optional.empty());

        InternalServerException exception = assertThrows(InternalServerException.class, () -> {
            movieService.getMovieByTitle("Nonexistent Movie");
        });
        assertEquals("Internal Error while getting movie with title: Nonexistent Movie", exception.getMessage());
        verify(movieRepository, times(1)).findMovieByTitle("Nonexistent Movie");
    }

    @Test
    void addMovie_success() {

        when(movieRepository.save(any(Movie.class))).thenReturn(sampleMovie);

        Movie result = movieService.addMovie(sampleMovieRequest);

        assertNotNull(result);
        assertEquals(sampleMovie.getTitle(), result.getTitle());
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void addMovie_WhenExceptionOccurs() {

        when(movieRepository.save(any(Movie.class))).thenThrow(new RuntimeException("Database error"));

        InternalServerException exception = assertThrows(InternalServerException.class, () -> {
            movieService.addMovie(sampleMovieRequest);
        });
        assertEquals("Internal Error while adding movie", exception.getMessage());
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void updateMovie_WhenMovieExists() {

        when(movieRepository.findMovieByTitle("Test Movie")).thenReturn(Optional.of(sampleMovie));
        when(movieRepository.save(any(Movie.class))).thenReturn(sampleMovie);

        MovieRequest updatedRequest = new MovieRequest("Updated Movie", "Comedy", 150, 5.0, 2024);

        Movie result = movieService.updateMovie("Test Movie", updatedRequest);

        assertNotNull(result);
        verify(movieRepository, times(1)).findMovieByTitle("Test Movie");
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void deleteMovie_WhenMovieExists() {

        when(movieRepository.findMovieByTitle("Test Movie")).thenReturn(Optional.of(sampleMovie));
        doNothing().when(movieRepository).delete(any(Movie.class));

        movieService.deleteMovie("Test Movie");

        // Assert
        verify(movieRepository, times(1)).findMovieByTitle("Test Movie");
        verify(movieRepository, times(1)).delete(any(Movie.class));
    }

    @Test
    void deleteMovie_WhenExceptionOccurs() {

        when(movieRepository.findMovieByTitle("Test Movie")).thenReturn(Optional.of(sampleMovie));
        doThrow(new RuntimeException("Database error")).when(movieRepository).delete(any(Movie.class));

        InternalServerException exception = assertThrows(InternalServerException.class, () -> {
            movieService.deleteMovie("Test Movie");
        });
        assertEquals("Internal Error while try to delete movie with title: Test Movie", exception.getMessage());
        verify(movieRepository, times(1)).findMovieByTitle("Test Movie");
        verify(movieRepository, times(1)).delete(any(Movie.class));
    }
}