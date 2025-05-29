package com.autoxpress.usedcarsalesportal.config;

import com.autoxpress.usedcarsalesportal.model.User;
import com.autoxpress.usedcarsalesportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initAdminUser() {
        return args -> {
            // Check if admin user already exists
            if (userRepository.findByUsername("admin") == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@autoxpress.com");
                admin.setContactNumber("+60123456789");
                admin.setRole("ADMIN"); // Explicitly set ADMIN role
                userRepository.save(admin);
                System.out.println("Default admin user created: username=admin, email=admin@autoxpress.com, role=ADMIN");
            } else {
                System.out.println("Admin user already exists, skipping creation.");
            }
        };
    }
}