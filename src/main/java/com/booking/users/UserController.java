package com.booking.users;

import com.booking.exceptions.InvalidPasswordException;
import com.booking.exceptions.UnAuthorizedUserException;
import com.booking.jwt.JwtUtils;
import com.booking.users.response.JwtResponse;
import com.booking.users.service.UserService;
import com.booking.users.request.ChangePasswordRequest;
import com.booking.users.request.LoginRequest;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Users")
@RestController
public class UserController {
    @Autowired
    private UserPrincipalService userPrincipalService;

    @Autowired
    private AdminRepository adminRepository;

    private UserService userService;

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//        String username = principal.getName();
        System.out.println("Logged in");
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            return ResponseEntity
                    .ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
        } catch(Exception e) {
            System.out.println(e);
        }

        return (ResponseEntity<?>) ResponseEntity.notFound();
    }

    @PutMapping(path = "/change-password")
    String changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, Principal principal) throws InvalidPasswordException, UnAuthorizedUserException {

        String oldPassword = changePasswordRequest.getOldPassword();

        String newPassword = changePasswordRequest.getNewPassword();

        if(newPassword.equals(oldPassword)){
            throw new InvalidPasswordException("Old password cannot be same as New password");
        }

        Admin admin = userService.getAdmin(principal.getName(), oldPassword);
        userService.updatePassword(admin.getUser().getId(), oldPassword, newPassword);
        return "Password Changed Successfully!";
    }
}
