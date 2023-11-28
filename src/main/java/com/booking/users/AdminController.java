package com.booking.users;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Api(tags = "Users")
@RestController
public class AdminController {
    @Autowired
    private UserPrincipalService userPrincipalService;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/profile")
    AdminResponse profile(Principal principal) {
        String username = principal.getName();
        Admin admin = userPrincipalService.findAdminByUsername(username);

        return new AdminResponse(admin.getUsername(), admin.getName(), admin.getCounterNo());
    }
}
