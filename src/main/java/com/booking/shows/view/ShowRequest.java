package com.booking.shows.view;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class ShowRequest {
    private BigDecimal cost;
    private String movieId;
    private List<Integer> slots;
    private Date date;


    public BigDecimal getCost() {
        return cost;
    }

    public String getMovieId() {
        return movieId;
    }

    public List<Integer> getSlots() {
        return slots;
    }

    public Date getDate() {
        return date;
    }
}
