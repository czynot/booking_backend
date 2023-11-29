package com.booking.users.view;


import com.booking.users.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserPrincipalServiceTest {
    private UserRepository userRepository;
    private AdminRepository adminRepository;
    private UserPrincipalService userPrincipalService;

    @BeforeEach
    public void beforeEach() {
        userRepository = mock(UserRepository.class);
        adminRepository = mock(AdminRepository.class);
        userPrincipalService = new UserPrincipalService(userRepository, adminRepository);
    }

    @Test
    public void shouldFindAdminByUsername() {
        Admin mockAdmin = mock(Admin.class);
        when(adminRepository.findByUsername("admin-user")).thenReturn(mockAdmin);

        Admin foundAdmin = userPrincipalService.findAdminByUsername("admin-user");

        assertThat(foundAdmin, is(equalTo(mockAdmin)));
    }

    @Test
    public void shouldThrowExceptionIfAdminNotFound() {
        when(adminRepository.findByUsername("admin-user")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userPrincipalService.findAdminByUsername("undefined-user"));
    }
}
