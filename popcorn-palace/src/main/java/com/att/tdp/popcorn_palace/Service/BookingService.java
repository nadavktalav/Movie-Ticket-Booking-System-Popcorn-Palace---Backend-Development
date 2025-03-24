package com.att.tdp.popcorn_palace.Service;

import com.att.tdp.popcorn_palace.Model.Booking;
import com.att.tdp.popcorn_palace.Model.Showtime;
import com.att.tdp.popcorn_palace.Repository.BookingRepository;
import com.att.tdp.popcorn_palace.Repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.Util.Exceptions.InternalServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {

    private BookingRepository bookingRepository;

    private ShowtimeRepository showtimeRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ShowtimeRepository showtimeRepository) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
    }

    @Transactional
    public String bookTicket(long showTimeId, int seatNumber, String userId) {
        try {

            // Use pessimistic locking to prevent other transactions from modifying the showtime concurrently
            Optional<Showtime> showtimeOptional = showtimeRepository.findByIdWithLock(showTimeId);
            if (showtimeOptional.isEmpty()) {
                throw new InternalServerException("Showtime not found");
            }
            Showtime showtime = showtimeOptional.get();

            // Ensure the showtime is not in the past
            if (showtime.getEndTime().isBefore(LocalDateTime.now())) {
                throw new InternalServerException("Showtime already ended");
            }

            // Check if the seat is already booked (using unique constraint in the database)
            if (bookingRepository.existsBookingBySeatNumberAndShowtime(seatNumber, showTimeId)) {
                throw new InternalServerException("Seat is already booked for this showtime");
            }

            // Create a new Booking object
            Booking booking = new Booking(showtime, seatNumber, userId);

            // Save the booking to the repository
            bookingRepository.save(booking);

            // Return the booking ID to the caller
            return booking.getId();

        } catch (InternalServerException | IllegalArgumentException e) {
            throw e; // Rethrow custom exception
        } catch (Exception e) {
            // Log the exception (optional)
            throw new InternalServerException("An unexpected error occurred while booking the ticket");
        }
    }



}







