package com.booking.users.service;

import com.booking.users.Admin;
import com.booking.users.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final AdminRepository adminRepository;

    @Autowired
    public UserService(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    public Admin getByUsername(String name){
        Admin admin = adminRepository.findByUsername(name);
        if(admin == null){
            throw new UsernameNotFoundException("user not found");
        }
        return admin;
    }
}
