package com.booking.users;

import com.booking.users.view.ChangePasswordRequest;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "Users")
@RestController
public class UserController {

    @GetMapping("/login")
    Map<String, Object> login(Principal principal) {
        String username = principal.getName();
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("username", username);
        return userDetails;
    }

    @PutMapping(path = "/change-password")
    String changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, Principal principal) {
        System.out.println(changePasswordRequest.getNewPassword());
        return changePasswordRequest.getNewPassword();
    }
}
