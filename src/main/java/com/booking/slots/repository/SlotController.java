package com.booking.slots.repository;

import com.booking.handlers.models.ErrorResponse;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.shows.view.models.ShowResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Date;
import java.util.List;


@Api(tags = "Slots")
@RestController
@RequestMapping("/slots")
public class SlotController {

    @Autowired
    private SlotService slotService;


    @GetMapping
    @ApiOperation(value = "Fetch All Available Slots")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Fetched available slots successfully"),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })
    public List<Slot> fetchAll () throws IOException, FormatException {
        return slotService.getAllSlots();
    }
}
