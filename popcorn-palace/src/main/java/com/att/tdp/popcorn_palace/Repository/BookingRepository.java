package com.att.tdp.popcorn_palace.Repository;

import com.att.tdp.popcorn_palace.Model.Booking;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Booking b WHERE b.seatNumber = :seatNumber AND b.showtime.id = :showtimeId")
    boolean existsBookingBySeatNumberAndShowtime(@Param("seatNumber") int seatNumber, @Param("showtimeId") long showtimeId);

}

