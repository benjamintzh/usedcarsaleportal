package com.autoxpress.usedcarsalesportal.service;

import com.autoxpress.usedcarsalesportal.dto.UserRegistrationDto;
import com.autoxpress.usedcarsalesportal.model.User;
import com.autoxpress.usedcarsalesportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(UserRegistrationDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername().toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setRole("USER");
        user.setContactNumber(dto.getContactNumber());
        System.out.println("Saving user: " + user.getUsername() + ", Role: " + user.getRole() + ", Hashed Password: " + user.getPassword());
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserRegistrationDto dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(dto.getUsername().toLowerCase());
        user.setEmail(dto.getEmail());
        user.setContactNumber(dto.getContactNumber());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        return userRepository.save(user);
    }

    public void makeAdmin(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole("ADMIN");
        userRepository.save(user);
    }

    public void setRole(Long userId, String role) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (!List.of("USER", "ADMIN").contains(role)) {
            throw new RuntimeException("Invalid role: " + role);
        }
        user.setRole(role);
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User findByUsername(String username) {
        System.out.println("Finding user by username: " + username.toLowerCase());
        User user = userRepository.findByUsername(username.toLowerCase());
        if (user == null) {
            System.out.println("User not found in findByUsername: " + username.toLowerCase());
        } else {
            System.out.println("User found in findByUsername: " + user.getUsername());
        }
        return user;
    }

    public List<String> getDistinctUsernamesWithAppointments() {
        return userRepository.findDistinctUsernamesWithAppointments();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Attempting to load user for authentication: " + username);
        User user = userRepository.findByUsername(username.toLowerCase());
        if (user == null) {
            System.out.println("User not found for authentication: " + username.toLowerCase());
            throw new UsernameNotFoundException("User not found: " + username.toLowerCase());
        }
        System.out.println("User found: " + user.getUsername() + ", Password: " + user.getPassword() + ", Role: " + user.getRole());
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}