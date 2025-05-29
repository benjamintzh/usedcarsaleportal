package com.autoxpress.usedcarsalesportal.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidAppointmentDto() {
        AppointmentDto dto = new AppointmentDto();
        dto.setCarId(1L);
        dto.setCarManufacture("Toyota");
        dto.setCarModel("Camry");
        dto.setUsername("testuser");
        dto.setEmail("test@example.com");
        dto.setAppointmentDate(LocalDateTime.now().plusDays(1).withHour(10));

        Set<ConstraintViolation<AppointmentDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidEmail() {
        AppointmentDto dto = new AppointmentDto();
        dto.setCarId(1L);
        dto.setCarManufacture("Toyota");
        dto.setCarModel("Camry");
        dto.setUsername("testuser");
        dto.setEmail("invalid-email");
        dto.setAppointmentDate(LocalDateTime.now().plusDays(1).withHour(10));

        Set<ConstraintViolation<AppointmentDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Invalid email format", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidTimeRange() {
        AppointmentDto dto = new AppointmentDto();
        dto.setCarId(1L);
        dto.setCarManufacture("Toyota");
        dto.setCarModel("Camry");
        dto.setUsername("testuser");
        dto.setEmail("test@example.com");
        dto.setAppointmentDate(LocalDateTime.now().plusDays(1).withHour(8)); // Before 9 AM

        Set<ConstraintViolation<AppointmentDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Appointment time must be between 9:00 AM and 5:00 PM", violations.iterator().next().getMessage());
    }
}