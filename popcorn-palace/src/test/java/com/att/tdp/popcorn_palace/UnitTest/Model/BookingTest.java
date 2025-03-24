package com.att.tdp.popcorn_palace.UnitTest.Model;
import com.att.tdp.popcorn_palace.Model.Booking;
import com.att.tdp.popcorn_palace.Model.Showtime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingTest {

    private Showtime showtime;
    private String userId;

    @BeforeEach
    public void setUp() {
        showtime = new Showtime();
        showtime.setId(1L);
        userId = "testUser";
    }

    @ParameterizedTest
    @ValueSource(ints = {0,10, 15, 20,1000000})
    void testBookingConstructorValid(int seatNumber) {
        Booking booking = new Booking(showtime, seatNumber, userId);

        assertNotNull(booking.getId(), "Booking ID should not be null");
        assertEquals(seatNumber, booking.getSeatNumber(), "Seat number should match");
        assertEquals(userId, booking.getUserId(), "User ID should match");
        assertEquals(showtime.getId(), booking.getShowtimeId(), "Showtime ID should match");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -10})
    void testBookingConstructorSeatNumberNegative(int seatNumber) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Booking(showtime, seatNumber, userId);
        });
        assertEquals("Seat number cannot be negative", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "   "})
    void testBookingConstructorUserIdEmpty(String invalidUserId) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Booking(showtime, 10, invalidUserId);
        });
        assertEquals("User ID must be at least one character", exception.getMessage());
    }

    @Test
    void testBookingConstructorUserIdNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Booking(showtime, 10, null);
        });
        assertEquals("User ID must be at least one character", exception.getMessage());
    }


}
