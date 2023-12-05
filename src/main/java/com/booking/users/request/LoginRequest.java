package com.booking.users.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class LoginRequest {

    @JsonProperty
    @ApiModelProperty(name = "username", value = "User Name", dataType = "java.lang.String", required = true, position = 1)
    @NotNull
    String username;

    @JsonProperty
    @ApiModelProperty(name = "password", value = "password", dataType = "java.lang.String", required = true, position = 1)
    @NotNull
    String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
