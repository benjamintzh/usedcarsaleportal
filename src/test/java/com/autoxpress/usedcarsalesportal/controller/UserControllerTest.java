package com.autoxpress.usedcarsalesportal.controller;

import com.autoxpress.usedcarsalesportal.config.TestSecurityConfig;
import com.autoxpress.usedcarsalesportal.dto.UserRegistrationDto;
import com.autoxpress.usedcarsalesportal.model.User;
import com.autoxpress.usedcarsalesportal.service.AppointmentService;
import com.autoxpress.usedcarsalesportal.service.CarService;
import com.autoxpress.usedcarsalesportal.service.EmailService;
import com.autoxpress.usedcarsalesportal.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private CarService carService;
    
    @MockBean
    private AppointmentService appointmentService;
    
    @MockBean
    private EmailService emailService;

    @Test
    void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/user/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        mockMvc.perform(post("/user/register")
                .param("username", "testuser")
                .param("password", "password")
                .param("email", "test@example.com")
                .param("contactNumber", "1234567890")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService, times(1)).saveUser(any(UserRegistrationDto.class));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testShowProfile() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        when(userService.findByUsername("testuser")).thenReturn(user);

        mockMvc.perform(get("/user/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("user", user));
    }
}