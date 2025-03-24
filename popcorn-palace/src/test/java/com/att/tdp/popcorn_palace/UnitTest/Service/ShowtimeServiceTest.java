package com.att.tdp.popcorn_palace.UnitTest.Service;


import com.att.tdp.popcorn_palace.Model.Movie;
import com.att.tdp.popcorn_palace.Model.Showtime;
import com.att.tdp.popcorn_palace.Repository.MovieRepository;
import com.att.tdp.popcorn_palace.Repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.Service.ShowtimeService;
import com.att.tdp.popcorn_palace.Util.Exceptions.InternalServerException;
import com.att.tdp.popcorn_palace.Util.Exceptions.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.Util.Requests.ShowtimeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ShowtimeServiceTest {

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private MovieRepository movieRepository;

    private ShowtimeService showtimeService;

    private Showtime sampleShowtime;
    private Movie sampleMovie;
    private ShowtimeRequest sampleShowtimeRequest;

    @BeforeEach
    void setUp() {
        showtimeService = new ShowtimeService(showtimeRepository, movieRepository);

        sampleMovie = new Movie();
        sampleMovie.setId(1L);
        sampleMovie.setTitle("Test Movie");
        sampleMovie.setGenre("Action");
        sampleMovie.setDuration(120);
        sampleMovie.setReleaseYear(2023);
        sampleMovie.setRating(4.5);

        sampleShowtime = new Showtime();
        sampleShowtime.setId(1L);
        sampleShowtime.setStartTime(LocalDateTime.now());
        sampleShowtime.setEndTime(LocalDateTime.now().plusHours(2));
        sampleShowtime.setTheater("Theater 1");


        sampleShowtimeRequest = new ShowtimeRequest(sampleMovie.getId(), 30.2,  "Theater 1", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
    }

    @Test
    void getShowtime_ShouldReturnShowtime() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(sampleShowtime));

        Showtime result = showtimeService.getShowtime(1L);

        assertNotNull(result);
        assertEquals(sampleShowtime.getId(), result.getId());
        verify(showtimeRepository, times(1)).findById(1L);
    }

    @Test
    void getShowtime_WhenShowtimeNotFound_ShouldThrowResourceNotFoundException() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            showtimeService.getShowtime(1L);
        });
        assertEquals("Showtime with id 1 was not found", exception.getMessage());
        verify(showtimeRepository, times(1)).findById(1L);
    }

    @Test
    void addShowtime_success() {
        when(showtimeRepository.findShowtimesByTimeRangeAndTheater(any(), any(), any())).thenReturn(List.of());
        when(movieRepository.findById(sampleShowtimeRequest.getMovieId())).thenReturn(Optional.of(sampleMovie));
        when(showtimeRepository.save(any(Showtime.class))).thenReturn(sampleShowtime);

        Showtime result = showtimeService.addShowtime(sampleShowtimeRequest);

        assertNotNull(result);
        assertEquals(sampleShowtime.getId(), result.getId());
        verify(showtimeRepository, times(1)).findShowtimesByTimeRangeAndTheater(any(), any(), any());
        verify(movieRepository, times(1)).findById(sampleShowtimeRequest.getMovieId());
        verify(showtimeRepository, times(1)).save(any(Showtime.class));
    }

    @Test
    void addShowtime_WhenShowtimeOverlaps() {
        when(showtimeRepository.findShowtimesByTimeRangeAndTheater(any(), any(), any())).thenReturn(Arrays.asList(sampleShowtime));

        InternalServerException exception = assertThrows(InternalServerException.class, () -> {
            showtimeService.addShowtime(sampleShowtimeRequest);
        });
        assertEquals("Showtime overlaps with existing showtime", exception.getMessage());
        verify(showtimeRepository, times(1)).findShowtimesByTimeRangeAndTheater(any(), any(), any());
    }

    @Test
    void addShowtime_WhenMovieNotFound() {
        when(showtimeRepository.findShowtimesByTimeRangeAndTheater(any(), any(), any())).thenReturn(List.of());
        when(movieRepository.findById(sampleShowtimeRequest.getMovieId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            showtimeService.addShowtime(sampleShowtimeRequest);
        });
        assertEquals("Movie with id " + sampleShowtimeRequest.getMovieId() + " was not found", exception.getMessage());
        verify(showtimeRepository, times(1)).findShowtimesByTimeRangeAndTheater(any(), any(), any());
        verify(movieRepository, times(1)).findById(sampleShowtimeRequest.getMovieId());
    }

    @Test
    void updateShowtime_success() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(sampleShowtime));
        when(movieRepository.findById(sampleShowtimeRequest.getMovieId())).thenReturn(Optional.of(sampleMovie));
        when(showtimeRepository.save(any(Showtime.class))).thenReturn(sampleShowtime);

        Showtime result = showtimeService.updateShowtime(1L, sampleShowtimeRequest);

        assertNotNull(result);
        verify(showtimeRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).findById(sampleShowtimeRequest.getMovieId());
        verify(showtimeRepository, times(1)).save(any(Showtime.class));
    }

    @Test
    void updateShowtime_WhenShowtimeNotFound() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            showtimeService.updateShowtime(1L, sampleShowtimeRequest);
        });
        assertEquals("showtime with id 1 was not founded", exception.getMessage());
        verify(showtimeRepository, times(1)).findById(1L);
    }

    @Test
    void updateShowtime_WhenMovieNotFound() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(sampleShowtime));
        when(movieRepository.findById(sampleShowtimeRequest.getMovieId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            showtimeService.updateShowtime(1L, sampleShowtimeRequest);
        });
        assertEquals("Movie with id " + sampleShowtimeRequest.getMovieId() + " was not found", exception.getMessage());
        verify(showtimeRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).findById(sampleShowtimeRequest.getMovieId());
    }

    @Test
    void deleteShowtime_success() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(sampleShowtime));
        doNothing().when(showtimeRepository).delete(any(Showtime.class));

        showtimeService.deleteShowtime(1L);

        verify(showtimeRepository, times(1)).findById(1L);
        verify(showtimeRepository, times(1)).delete(any(Showtime.class));
    }

    @Test
    void deleteShowtime_WhenShowtimeNotFound() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.empty());

        InternalServerException exception = assertThrows(InternalServerException.class, () -> {
            showtimeService.deleteShowtime(1L);
        });
        assertEquals("Internal Error while try to delete showtime with id: 1", exception.getMessage());
        verify(showtimeRepository, times(1)).findById(1L);
    }
}