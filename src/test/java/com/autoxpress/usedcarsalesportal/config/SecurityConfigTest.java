package com.autoxpress.usedcarsalesportal.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.autoxpress.usedcarsalesportal.model.User;
import com.autoxpress.usedcarsalesportal.service.AppointmentService;
import com.autoxpress.usedcarsalesportal.service.CarService;
import com.autoxpress.usedcarsalesportal.service.EmailService;
import com.autoxpress.usedcarsalesportal.service.UserService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

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
    void testPublicAccess() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "john", roles = "USER")
    void testUserProfileView() throws Exception {
        User mockUser = new User();
        mockUser.setUsername("john");
        mockUser.setEmail("john@example.com");

        when(userService.findByUsername("john")).thenReturn(mockUser);

        mockMvc.perform(get("/user/profile"))
               .andExpect(status().isOk())
               .andExpect(view().name("profile"))
               .andExpect(model().attributeExists("user"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void testAdminAccess() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk());
    }

    @Test
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/user/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }
}