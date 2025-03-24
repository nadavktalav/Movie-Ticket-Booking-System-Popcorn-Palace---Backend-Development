package com.att.tdp.popcorn_palace.UnitTest.Service;


import com.att.tdp.popcorn_palace.Model.Booking;
import com.att.tdp.popcorn_palace.Model.Showtime;
import com.att.tdp.popcorn_palace.Repository.BookingRepository;
import com.att.tdp.popcorn_palace.Repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.Service.BookingService;
import com.att.tdp.popcorn_palace.Util.Exceptions.InternalServerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ShowtimeRepository showtimeRepository;

    @InjectMocks
    private BookingService bookingService;

    private Showtime sampleShowtime;
    private Booking sampleBooking;



    private final long SHOWTIME_ID = 1L;
    private final int SEAT_NUMBER = 42;
    private final String USER_ID = "user123";

    @BeforeEach
    void setUp() {
        showtimeRepository.deleteAll();
        sampleShowtime = new Showtime();
        sampleShowtime.setId(1L);
        sampleShowtime.setStartTime(LocalDateTime.now().plusHours(2));
        sampleShowtime.setEndTime(LocalDateTime.now().plusHours(3));

        sampleBooking = new Booking();
        sampleBooking.setId("123");
        sampleBooking.setSeatNumber(1);
        sampleBooking.setUserId("user123");
    }

    @Test
    void bookTicket_WhenShowtimeNotFound() {
        when(showtimeRepository.findByIdWithLock(1L)).thenReturn(Optional.empty());

        InternalServerException exception = assertThrows(InternalServerException.class, () -> {
            bookingService.bookTicket(1L, 1, "user123");
        });

        assertEquals("Showtime not found", exception.getMessage());
        verify(showtimeRepository, times(1)).findByIdWithLock(1L);
    }

    @Test
    void bookTicket_WhenShowtimeHasEnded() {
        sampleShowtime.setEndTime(LocalDateTime.now().minusHours(1));
        when(showtimeRepository.findByIdWithLock(1L)).thenReturn(Optional.of(sampleShowtime));

        InternalServerException exception = assertThrows(InternalServerException.class, () -> {
            bookingService.bookTicket(1L, 1, "user123");
        });

        assertEquals("Showtime already ended", exception.getMessage());
        verify(showtimeRepository, times(1)).findByIdWithLock(1L);
    }

    @Test
    void bookTicket_WhenSeatAlreadyBooked() {
        when(showtimeRepository.findByIdWithLock(1L)).thenReturn(Optional.of(sampleShowtime));
        when(bookingRepository.existsBookingBySeatNumberAndShowtime(1, 1L)).thenReturn(true);

        InternalServerException exception = assertThrows(InternalServerException.class, () -> {
            bookingService.bookTicket(1L, 1, "user123");
        });

        assertEquals("Seat is already booked for this showtime", exception.getMessage());
        verify(showtimeRepository, times(1)).findByIdWithLock(1L);
        verify(bookingRepository, times(1)).existsBookingBySeatNumberAndShowtime(1, 1L);
    }

    @Test
    void bookTicket_successful() {
        when(showtimeRepository.findByIdWithLock(1L)).thenReturn(Optional.of(sampleShowtime));
        when(bookingRepository.existsBookingBySeatNumberAndShowtime(1, 1L)).thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenReturn(sampleBooking);

        String result = bookingService.bookTicket(1L, 1, "user123");

        assertFalse(result.isEmpty());
        verify(showtimeRepository, times(1)).findByIdWithLock(1L);
        verify(bookingRepository, times(1)).existsBookingBySeatNumberAndShowtime(1, 1L);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void bookTicket_ConcurrentRequests_OnlyOneSucceeds() throws InterruptedException {
        // Arrange
        final int THREAD_COUNT = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // Mock repository behaviors
        when(showtimeRepository.findByIdWithLock(SHOWTIME_ID))
                .thenReturn(Optional.of(sampleShowtime));

        // First call returns false (seat not booked), subsequent calls return true (seat already booked)
        // We use a counter to simulate this behavior
        final AtomicInteger callCount = new AtomicInteger(0);
        when(bookingRepository.existsBookingBySeatNumberAndShowtime(eq(SEAT_NUMBER), eq(SHOWTIME_ID)))
                .thenAnswer(invocation -> {
                    // First thread sees the seat as available
                    if (callCount.getAndIncrement() == 0) {
                        return false;
                    }
                    // Second thread sees the seat as booked
                    return true;
                });

        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocation -> {
                    Booking booking = invocation.getArgument(0);
                    // Simulate generated UUID
                    booking.setId("generated-uuid");
                    return booking;
                });

        // Act: Submit two concurrent booking attempts for the same seat
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    bookingService.bookTicket(SHOWTIME_ID, SEAT_NUMBER, USER_ID);
                    successCount.incrementAndGet();
                } catch (InternalServerException e) {
                    if (e.getMessage().contains("already booked")) {
                        failCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // Wait for all threads to complete
        latch.await();
        executorService.shutdown();

        // Assert
        assertEquals(1, successCount.get(), "Only one thread should succeed in booking the seat");
        assertEquals(1, failCount.get(), "One thread should fail with 'already booked' error");

        // Verify method invocation counts
        verify(showtimeRepository, times(2)).findByIdWithLock(SHOWTIME_ID);
        verify(bookingRepository, times(2)).existsBookingBySeatNumberAndShowtime(eq(SEAT_NUMBER), eq(SHOWTIME_ID));
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

}