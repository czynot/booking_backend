package com.booking.users;

import com.booking.exceptions.InvalidPasswordException;
import com.booking.exceptions.UnAuthorizedUserException;
import com.booking.users.service.UserService;
import com.booking.users.view.ChangePasswordRequest;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserPrincipalService userPrincipalService;

    @Autowired
    private AdminRepository adminRepository;

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/login")
    Map<String, Object> login(Principal principal) {
        String username = principal.getName();

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("username", username);

        return userDetails;
    }

    @PutMapping(path = "/change-password")
    String changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, Principal principal) throws InvalidPasswordException, UnAuthorizedUserException {

        if(changePasswordRequest.getNewPassword().equals(changePasswordRequest.getOldPassword())){
            throw new InvalidPasswordException("Old password cannot be same as New password");
        }

        Admin admin = userService.getAdmin(principal.getName());
        userService.updatePassword(admin.getUser().getId(),changePasswordRequest.getOldPassword(),changePasswordRequest.getNewPassword());
        return "Password Changed Successfully!";
    }
}
