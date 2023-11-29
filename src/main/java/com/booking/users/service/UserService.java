package com.booking.users.service;

import com.booking.exceptions.InvalidPasswordException;
import com.booking.exceptions.UnAuthorizedUserException;
import com.booking.passwordHistory.PasswordHistory;
import com.booking.passwordHistory.repository.PasswordHistoryRepository;
import com.booking.users.Admin;
import com.booking.users.AdminRepository;
import com.booking.users.User;
import com.booking.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final AdminRepository adminRepository;

    private final PasswordHistoryRepository passwordHistoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserService(AdminRepository adminRepository, PasswordHistoryRepository passwordHistoryRepository, UserRepository userRepository){
        this.adminRepository = adminRepository;
        this.passwordHistoryRepository = passwordHistoryRepository;
        this.userRepository = userRepository;
    }

    public Admin getAdmin(String name,String oldPassword) throws UnAuthorizedUserException {
        Admin admin = adminRepository.findByUsername(name);
        if(admin == null || !admin.getUser().getPassword().equals(oldPassword)){
            throw new UnAuthorizedUserException();
        }
        return admin;
    }
    public List<PasswordHistory> getPasswordHistory(Long user_id){
        return passwordHistoryRepository.findByUserId(user_id);
    }

    public boolean validateNewPassword(List<PasswordHistory> passwordHistories, String newPassword) {
        for(PasswordHistory oldPassword: passwordHistories){
            if(oldPassword.getPassword().equals(newPassword)){
                return true;
            }
        }
        return false;
    }

    public void deletePasswordFromHistory(List<PasswordHistory> passwordHistories) {
        if(passwordHistories.size() == 3 ) {
            passwordHistoryRepository.deleteById(passwordHistories.get(0).getId());
        }
    }

    public void updatePassword(long userId, String oldPassword, String newPassword) throws InvalidPasswordException {

        List<PasswordHistory> passwordHistories = getPasswordHistory(userId);
        if(validateNewPassword(passwordHistories,newPassword)){
            throw new InvalidPasswordException("New password cannot be same as last three passwords");
        }

        User user = userRepository.getById(userId);
        user.setPassword(newPassword);
        userRepository.save(user);

        deletePasswordFromHistory(passwordHistories);
        passwordHistoryRepository.save(new PasswordHistory(user,oldPassword));
    }
}
