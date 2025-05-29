package com.autoxpress.usedcarsalesportal.service;

import com.autoxpress.usedcarsalesportal.dto.UserRegistrationDto;
import com.autoxpress.usedcarsalesportal.model.User;
import com.autoxpress.usedcarsalesportal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser_Success() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("testuser");
        dto.setPassword("password");
        dto.setEmail("test@example.com");
        dto.setContactNumber("1234567890");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        userService.saveUser(dto);

        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    void testUpdateUser_Success() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("testuser");
        existingUser.setEmail("old@example.com");

        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("testuser");
        dto.setEmail("new@example.com");
        dto.setContactNumber("1234567890");

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        userService.updateUser(1L, dto);

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals("new@example.com", existingUser.getEmail());
    }

    @Test
    void testMakeAdmin_Success() {
        User user = new User();
        user.setId(1L);
        user.setRole("USER");

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.makeAdmin(1L);

        verify(userRepository, times(1)).save(user);
        assertEquals("ADMIN", user.getRole());
    }
}