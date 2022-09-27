package com.booking;

import com.booking.users.Admin;
import com.booking.users.AdminRepository;
import com.booking.users.User;
import com.booking.users.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository repository, AdminRepository adminRepository) {
        return args -> {
            if (repository.findByUsername("seed-user-1").isEmpty()) {
                User user1 = repository.save(new User("seed-user-1", "foobar"));
                adminRepository.save(new Admin(user1, "Admin User 1", 1));
            }
            if (repository.findByUsername("seed-user-2").isEmpty()) {
                User user2 = repository.save(new User("seed-user-2", "foobar"));
                adminRepository.save(new Admin(user2, "Admin User 2", 2));
            }
        };
    }
}
