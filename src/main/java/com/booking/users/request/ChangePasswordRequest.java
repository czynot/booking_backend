package com.booking.users.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ChangePasswordRequest {
    @JsonProperty
    @ApiModelProperty(name = "oldPassword", value = "old password", dataType = "java.lang.String", required = true, position = 1)
    @NotNull
    private String oldPassword;

    @JsonProperty
    @ApiModelProperty(name = "newPassword", value = "new password", dataType = "java.lang.String",required = true, position = 2)
    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,64}$", message = "Password constraint")
    private String newPassword;

    public ChangePasswordRequest() {}

    public ChangePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }
}
