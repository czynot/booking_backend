package com.booking.movieGateway;

import com.booking.handlers.models.ErrorResponse;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.movieGateway.models.Movie;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Api(tags = "Movies")
@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieGateway movieGateway;

    @Autowired
    public MovieController(MovieGateway movieGateway) {
        this.movieGateway = movieGateway;
    }

    @GetMapping
    @ApiOperation(value = "Fetch Movies", response = Movie.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Fetched movies successfully", response = Movie.class),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })
    public ResponseEntity<List<Movie>> fetchMovies() {
        try {
            List<Movie> movies = movieGateway.getAllMovies();
            System.out.println(movies.size());
            return new ResponseEntity<>(movies, HttpStatus.OK);
        } catch (IOException | FormatException e) {
            // Handle exception appropriately, you might want to log the error
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
