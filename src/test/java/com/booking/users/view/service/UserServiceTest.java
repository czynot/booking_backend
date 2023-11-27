package com.booking.users.view.service;

import com.booking.exceptions.InvalidPasswordException;
import com.booking.exceptions.UnAuthorizedUserException;
import com.booking.passwordHistory.PasswordHistory;
import com.booking.passwordHistory.repository.PasswordHistoryRepository;
import com.booking.users.Admin;
import com.booking.users.AdminRepository;
import com.booking.users.User;
import com.booking.users.UserRepository;
import com.booking.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UserServiceTest {
    private AdminRepository adminRepository;
    private PasswordHistoryRepository passwordHistoryRepository;

    private UserRepository userRepository;

    @BeforeEach
    private void beforeEach(){
        adminRepository = mock(AdminRepository.class);
        passwordHistoryRepository = mock(PasswordHistoryRepository.class);
        userRepository = mock(UserRepository.class);
    }

    @Test
    public void should_return_admin_by_username() throws UnAuthorizedUserException {
        Admin admin = mock(Admin.class);
        when(adminRepository.findByUsername("test-user")).thenReturn(admin);
        UserService userService = new UserService(adminRepository,passwordHistoryRepository, userRepository);

        Admin actual = userService.getAdmin("test-user");

        verify(adminRepository,times(1)).findByUsername("test-user");
        assertEquals(actual, admin);
    }


    @Test
    public void should_return_list_of_password_histories(){
        User user = new User("test-username", "test-password");
        List<PasswordHistory> passwordHistory = new ArrayList<>(){{
            add(new PasswordHistory(user, "test-password"));
            add(new PasswordHistory(user, "test-password2"));
            add(new PasswordHistory(user, "test-password3"));
        }};
        when(passwordHistoryRepository.findByUserId(1L)).thenReturn(passwordHistory);
        UserService userService = new UserService(adminRepository, passwordHistoryRepository, userRepository);

        List<PasswordHistory> actual = userService.getPasswordHistory(1L);

        verify(passwordHistoryRepository,times(1)).findByUserId(1L);
        assertEquals(actual, passwordHistory);
    }

    @Test
    public void shouldReturnTrueWhenTheHistoryOfPasswordsContainsNewPassword(){
        User user = new User("test-username", "test-password");
        List<PasswordHistory> passwordHistory = new ArrayList<>(){{
            add(new PasswordHistory(user,"test-password"));
            add(new PasswordHistory(user, "test-password2"));
            add(new PasswordHistory(user, "test-password3"));
        }};
        when(passwordHistoryRepository.findByUserId(1L)).thenReturn(passwordHistory);
        UserService userService = new UserService(adminRepository, passwordHistoryRepository, userRepository);
        List<PasswordHistory> passwordHistories = userService.getPasswordHistory(1L);

        boolean isValid = userService.validateNewPassword(passwordHistories, "test-password");

        assertTrue(isValid);
    }

    @Test
    public void shouldDeleteTheOldPasswordFromPasswordHistory(){
        User user = new User("test-username", "test-password");
        List<PasswordHistory> passwordHistory = new ArrayList<>(){{
            add(new PasswordHistory(user,"test-password"));
            add(new PasswordHistory(user, "test-password2"));
            add(new PasswordHistory(user, "test-password3"));
        }};
        when(passwordHistoryRepository.findByUserId(1L)).thenReturn(passwordHistory);
        UserService userService = new UserService(adminRepository, passwordHistoryRepository, userRepository);
        List<PasswordHistory> passwordHistories = userService.getPasswordHistory(1L);

        userService.deletePasswordFromHistory(passwordHistories);

        verify(passwordHistoryRepository, times(1)).deleteById(passwordHistories.get(0).getId());
    }

    @Test
    public void shouldUpdateThePasswordAfterItIsValidated() throws InvalidPasswordException {
        User user = new User("test-username", "test-password");
        List<PasswordHistory> passwordHistory = new ArrayList<>(){{
            add(new PasswordHistory(user,"test-password"));
            add(new PasswordHistory(user, "test-password2"));
            add(new PasswordHistory(user, "test-password3"));
        }};
        when(passwordHistoryRepository.findByUserId(1L)).thenReturn(passwordHistory);
        when(userRepository.getById(1L)).thenReturn(user);
        UserService userService = new UserService(adminRepository, passwordHistoryRepository, userRepository);
        List<PasswordHistory> passwordHistories = userService.getPasswordHistory(1L);

        userService.updatePassword(1L, "test-password", "test-password4");

        verify(userRepository).save(new User("test-username", "test-password4"));

    }

    @Test
    public void shouldDeletePasswordHistoryAndUpdateOldPasswordInPasswordHistory() throws InvalidPasswordException {
        User user = new User("test-username", "test-password");
        List<PasswordHistory> passwordHistory = new ArrayList<>(){{
            add(new PasswordHistory(user,"test-password"));
            add(new PasswordHistory(user, "test-password2"));
            add(new PasswordHistory(user, "test-password3"));
        }};
        when(passwordHistoryRepository.findByUserId(1L)).thenReturn(passwordHistory);
        when(userRepository.getById(1L)).thenReturn(user);
        UserService userService = new UserService(adminRepository, passwordHistoryRepository, userRepository);
        List<PasswordHistory> passwordHistories = userService.getPasswordHistory(1L);

        userService.updatePassword(1L, "test-password", "test-password4");

        verify(passwordHistoryRepository).save(new PasswordHistory(user, "test-password"));
    }

}
