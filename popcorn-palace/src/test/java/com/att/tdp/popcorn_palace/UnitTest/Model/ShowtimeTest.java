package com.att.tdp.popcorn_palace.UnitTest.Model;

import com.att.tdp.popcorn_palace.Model.Movie;
import com.att.tdp.popcorn_palace.Model.Showtime;
import com.att.tdp.popcorn_palace.Util.Requests.MovieRequest;
import com.att.tdp.popcorn_palace.Util.Requests.ShowtimeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ShowtimeTest {

    private Movie movie;
    private ShowtimeRequest validShowtimeRequest;
    private ShowtimeRequest invalidShowtimeRequest;

    @BeforeEach
    void setUp() {
        MovieRequest movieRequest = new MovieRequest("Test Movie", "Action", 120, 8.5, 2023);
        movie = new Movie(movieRequest);
        movie.setId(1L);

        // Valid ShowtimeRequest with appropriate fields
        validShowtimeRequest = new ShowtimeRequest(
                1L,
                10.0,
                "Theater 1",
                LocalDateTime.of(2023, 10, 12, 14, 30), // Start time
                LocalDateTime.of(2023, 10, 12, 16, 30) // End time
        );

        // Invalid ShowtimeRequest (endTime before startTime)
        invalidShowtimeRequest = new ShowtimeRequest(
                1L,
                10.0,
                "Theater 1",
                LocalDateTime.of(2023, 10, 12, 16, 30), // Start time
                LocalDateTime.of(2023, 10, 12, 14, 30) // End time (invalid)
        );
    }

    @Test
    void testShowtimeConstructorValidCase() {
        Showtime showtime = new Showtime(validShowtimeRequest, movie);

        assertNotNull(showtime);
        assertEquals(validShowtimeRequest.getStartTime(), showtime.getStartTime());
        assertEquals(validShowtimeRequest.getEndTime(), showtime.getEndTime());
        assertEquals(validShowtimeRequest.getPrice(), showtime.getPrice());
        assertEquals(validShowtimeRequest.getTheater(), showtime.getTheater());
        assertEquals(movie.getId(), showtime.getMovieId());
    }

    @Test
    void testShowtimeConstructorInvalidEndTime() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Showtime(invalidShowtimeRequest, movie));
        assertTrue(exception.getMessage().contains("Showtime must begin before it ends"));
    }

    @Test
    void testShowtimeConstructorInvalidPrice() {
        ShowtimeRequest invalidPriceRequest = new ShowtimeRequest(
                1L,
                -5.0, // Invalid negative price
                "Theater 1",
                LocalDateTime.of(2023, 10, 12, 14, 30),
                LocalDateTime.of(2023, 10, 12, 16, 30)
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Showtime(invalidPriceRequest, movie));
        assertTrue(exception.getMessage().contains("Price cannot be negative"));
    }

    @Test
    void testShowtimeConstructorInvalidMovieId() {
        ShowtimeRequest invalidMovieIdRequest = new ShowtimeRequest(
                -1L, // Invalid Movie ID
                10.0,
                "Theater 1",
                LocalDateTime.of(2023, 10, 12, 14, 30),
                LocalDateTime.of(2023, 10, 12, 16, 30)
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Showtime(invalidMovieIdRequest, movie));
        assertTrue(exception.getMessage().contains("Showtime Movie ID must be negative"));
    }

    @Test
    void testShowtimeConstructorInvalidTheater() {
        ShowtimeRequest invalidTheaterRequest = new ShowtimeRequest(
                1L,
                10.0,
                "  ", // Invalid empty theater
                LocalDateTime.of(2023, 10, 12, 14, 30),
                LocalDateTime.of(2023, 10, 12, 16, 30)
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Showtime(invalidTheaterRequest, movie));
        assertTrue(exception.getMessage().contains("Showtime theater name must be at least one character"));
    }

    @Test
    void testUpdateShowtimeValid() {
        Showtime showtime = new Showtime(validShowtimeRequest, movie);

        ShowtimeRequest updatedShowtimeRequest = new ShowtimeRequest(
                1L,
                12.0, // Updated price
                "Updated Theater", // Updated theater
                LocalDateTime.of(2023, 10, 12, 16, 00), // Updated start time
                LocalDateTime.of(2023, 10, 12, 18, 00) // Updated end time
        );

        showtime.update(updatedShowtimeRequest, movie);

        assertEquals(12.0, showtime.getPrice());
        assertEquals("Updated Theater", showtime.getTheater());
        assertEquals(LocalDateTime.of(2023, 10, 12, 16, 00), showtime.getStartTime());
        assertEquals(LocalDateTime.of(2023, 10, 12, 18, 00), showtime.getEndTime());
    }

    @Test
    void testUpdateShowtimeInvalidEndTime() {
        Showtime showtime = new Showtime(validShowtimeRequest, movie);

        ShowtimeRequest invalidUpdateRequest = new ShowtimeRequest(
                1L,
                15.0,
                "Updated Theater",
                LocalDateTime.of(2023, 10, 12, 18, 00), // Updated start time
                LocalDateTime.of(2023, 10, 12, 16, 00) // Invalid end time (before start time)
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> showtime.update(invalidUpdateRequest, movie));
        assertTrue(exception.getMessage().contains("Showtime must begin before it ends"));
    }
}