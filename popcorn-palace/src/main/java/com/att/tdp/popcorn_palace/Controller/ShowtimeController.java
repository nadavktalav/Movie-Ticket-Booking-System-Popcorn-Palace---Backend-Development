package com.att.tdp.popcorn_palace.Controller;

import com.att.tdp.popcorn_palace.Model.Movie;
import com.att.tdp.popcorn_palace.Model.Showtime;
import com.att.tdp.popcorn_palace.Repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.Service.MovieService;
import com.att.tdp.popcorn_palace.Service.ShowtimeService;
import com.att.tdp.popcorn_palace.Util.Exceptions.InternalServerException;
import com.att.tdp.popcorn_palace.Util.Exceptions.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.Util.Requests.MovieRequest;
import com.att.tdp.popcorn_palace.Util.Requests.ShowtimeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {

    @Autowired
    public ShowtimeController(ShowtimeService showtimeService){
       this.showtimeService=showtimeService;
    }

    private ShowtimeService showtimeService;


    @GetMapping("/{showtimeId}")
    public ResponseEntity<?> getAllShowtimes(@PathVariable long showtimeId) {
        try {
            Showtime showtime = showtimeService.getShowtime(showtimeId);
            return ResponseEntity.ok(showtime);
        } catch (InternalServerException e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @PostMapping()
    public ResponseEntity<?> addShowtime(@RequestBody ShowtimeRequest showtimeRequest) {
        try {
            Showtime showtime = showtimeService.addShowtime(showtimeRequest);
            return ResponseEntity.ok(showtime);
        } catch (InternalServerException e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @PostMapping("update/{showtimeId}")
    public ResponseEntity<?> updateShowtime(@PathVariable long showtimeId, @RequestBody ShowtimeRequest showtimeRequest) {
        try {
            showtimeService.updateShowtime(showtimeId, showtimeRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InternalServerException e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @DeleteMapping("/{showtimeId}")
    public ResponseEntity<?> deleteShowtime(@PathVariable long showtimeId) {
        try {
            showtimeService.deleteShowtime(showtimeId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InternalServerException e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred."));
        }
    }
}
