package com.booking.shows.view;

import com.booking.handlers.models.ErrorResponse;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.movieGateway.models.Movie;
import com.booking.shows.ShowService;
import com.booking.shows.respository.Show;
import com.booking.shows.view.models.ShowResponse;
import com.booking.slots.repository.Slot;
import com.booking.slots.repository.SlotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "Shows")
@RestController
@RequestMapping("/shows")
public class ShowController {
    private final ShowService showService;
    private final SlotService slotService;

    @Autowired
    public ShowController(ShowService showService, SlotService slotService) {
        this.showService = showService;
        this.slotService = slotService;
    }

    @GetMapping
    @ApiOperation(value = "Fetch shows")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Fetched shows successfully"),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })
    public List<ShowResponse> fetchAll(@Valid @RequestParam(name = "date") Date date) throws IOException, FormatException {
        List<ShowResponse> showResponse = new ArrayList<>();
        for (Show show : showService.fetchAll(date)) {
            showResponse.add(constructShowResponse(show));
        }
        return showResponse;
    }

    private ShowResponse constructShowResponse(Show show) throws IOException, FormatException {
        Movie movie = showService.getMovieById(show.getMovieId());
        return new ShowResponse(movie, show.getSlot(), show);
    }

    @PostMapping
    @ApiOperation(value = "Add new shows to DB")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Added new show successfully to DB"),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })
    public ShowRequest addShow(@Valid @RequestBody ShowRequest showRequest) throws IOException, FormatException {
        for (int slotId : showRequest.getSlots()){
            System.out.println("Slot = " + slotId);
            Slot slot = slotService.getSlotById(slotId);

            Show newShow= new Show(showRequest.getDate(),slot, showRequest.getCost(), showRequest.getMovieId());
            System.out.println("New movie id = " + newShow.getMovieId());
            showService.addNewShow(newShow);
        }
        return showRequest;
    }
}
