package com.booking.users.view.service;

import com.booking.users.Admin;
import com.booking.users.AdminRepository;
import com.booking.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;


public class UserServiceTest {
    private AdminRepository adminRepository;

    @BeforeEach
    private void beforeEach(){
        adminRepository = mock(AdminRepository.class);
    }

    @Test
    public void should_return_admin_by_username(){
        Admin admin = mock(Admin.class);
        when(adminRepository.findByUsername("test-user")).thenReturn(admin);
        UserService userService = new UserService(adminRepository);

        userService.getByUsername("test-user");

        verify(adminRepository,times(1)).findByUsername("test-user");

    }


}
