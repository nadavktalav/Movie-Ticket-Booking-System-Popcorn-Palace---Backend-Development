package com.att.tdp.popcorn_palace.Model;

import com.att.tdp.popcorn_palace.Util.Exceptions.InternalServerException;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(
        name = "booking",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"showtime_id", "seat_number"})
        }
)
public class Booking {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY) // Represents the relationship with Showtime entity
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime; // Use the Showtime entity directly

    @Column(nullable = false, unique = true)
    private int seatNumber;

    @Column(nullable = false)
    private String userId;

    public long getShowtimeId() {
        return showtime.getId();
    }



    public int getSeatNumber() {
        return seatNumber;
    }

    public String getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Booking(){}

    public Booking( Showtime showtime , int seatNumber, String userId ){
        if (seatNumber < 0 ){
            throw new IllegalArgumentException("Seat number cannot be negative");
        }

        if (userId == null || userId.trim().isEmpty() ){
            throw  new IllegalArgumentException("User ID must be at least one character");
        }
        this.showtime =showtime;
        this.id = UUID.randomUUID().toString();
        if (id == null || id.isEmpty() ){
            throw new InternalServerException("Error while try generate id to the booking");
        }
        this.seatNumber = seatNumber;
        this.userId = userId;
    }

}