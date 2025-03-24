package com.att.tdp.popcorn_palace.UnitTest.Model;
import com.att.tdp.popcorn_palace.Model.Movie;
import com.att.tdp.popcorn_palace.Util.Requests.MovieRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MovieTest {

    private MovieRequest movieRequest;

    @BeforeEach
    public void setUp() {
        movieRequest = new MovieRequest("Test Movie","Action",120,8.5,2023);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Action", "Comedy", "Drama"})
    void testMovieConstructorValidGenre(String genre) {
        movieRequest.setGenre(genre);
        Movie movie = new Movie(movieRequest);
        movie.setId((long)2);
        assertNotNull(movie.getId(), "Movie ID should not be null");
        assertEquals(genre, movie.getGenre(), "Genre should match");
    }

    @Test
    void testMovieConstructorValid() {
        movieRequest.setGenre("Action");
        Movie movie = new Movie(movieRequest);
        movie.setId((long)1);
        assertEquals("Test Movie", movie.getTitle(), "Title should match");
        assertEquals("Action", movie.getGenre(), "Genre should match");
        assertEquals(120, movie.getDuration(), "Duration should match");
        assertEquals(8.5, movie.getRating(), "Rating should match");
        assertEquals(2023, movie.getReleaseYear(), "Release year should match");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void testMovieConstructorInvalidDuration(int invalidDuration) {
        movieRequest.setDuration(invalidDuration);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Movie(movieRequest);
        });
        assertEquals("movie duration must be positive number", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -0.1})
    void testMovieConstructorInvalidRating(double invalidRating) {
        movieRequest.setRating(invalidRating);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Movie(movieRequest);
        });
        assertEquals("movie rating cannot be negative", exception.getMessage());
    }

    @Test
    void testMovieConstructorMissingGenre() {
        movieRequest.setGenre("   ");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Movie(movieRequest);
        });
        assertEquals("movie must contain a genre", exception.getMessage());
    }

    @Test
    void testMovieConstructorMissingTitle() {
        movieRequest.setTitle(" ");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Movie(movieRequest);
        });
        assertEquals("movie must contain a title with at least one character", exception.getMessage());
    }

    @Test
    void testMovieConstructorNullTitle() {
        movieRequest.setTitle(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Movie(movieRequest);
        });
        assertEquals("movie must contain a title with at least one character", exception.getMessage());
    }

    @Test
    void testMovieConstructorNullGenre() {
        movieRequest.setGenre(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Movie(movieRequest);
        });
        assertEquals("movie must contain a genre", exception.getMessage());
    }
}