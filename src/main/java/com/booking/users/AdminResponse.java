package com.booking.users;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AdminResponse {
    @JsonProperty
    private final String username;
    @JsonProperty
    private final String name;
    @JsonProperty
    private final Integer counter;

    public AdminResponse(String username, String name, Integer counter) {
        this.username = username;
        this.name = name;
        this.counter = counter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminResponse that = (AdminResponse) o;
        return Objects.equals(username, that.username) && Objects.equals(name, that.name) && Objects.equals(counter, that.counter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name, counter);
    }
}
