package com.att.tdp.popcorn_palace.Controller;
import com.att.tdp.popcorn_palace.Service.BookingService;
import com.att.tdp.popcorn_palace.Util.Exceptions.InternalServerException;
import com.att.tdp.popcorn_palace.Util.Exceptions.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.Util.Requests.BookingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    private BookingService bookingService;

    @PostMapping()
    public ResponseEntity<?> bookTicket(@RequestBody BookingRequest bookingRequest) {
        try{
            long showtimeId = bookingRequest.getShowtimeId();
            int seatNumber = bookingRequest.getSeatNumber();
            String userId = bookingRequest.getUserId();
            String bookingId = bookingService.bookTicket(showtimeId, seatNumber,  userId);
            Map<String, String> response = new HashMap<>();
            response.put("bookingId", bookingId);
            return new ResponseEntity<Map>(response, HttpStatus.OK);
        }

        catch (InternalServerException | ResourceNotFoundException | IllegalArgumentException e){
//            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
        catch (Exception e){
            return new ResponseEntity<String>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


