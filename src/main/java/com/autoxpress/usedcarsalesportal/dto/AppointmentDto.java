package com.autoxpress.usedcarsalesportal.dto;

import com.autoxpress.usedcarsalesportal.validation.TimeRange;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class AppointmentDto {
    @NotNull(message = "Car ID is required")
    private Long carId;
    
    @NotEmpty(message = "Car manufacture is required")
    private String carManufacture;
    
    @NotEmpty(message = "Car model is required")
    private String carModel;
    
    @NotEmpty(message = "Username is required")
    private String username;
    
    private String contactNumber; // Made optional to isolate issue
    
    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email is required")
    private String email;
    
    @Future(message = "Appointment date must be in the future")
    @TimeRange(startHour = 9, endHour = 17, message = "Appointment time must be between 9:00 AM and 5:00 PM")
    private LocalDateTime appointmentDate;

    // Getters and Setters
    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }
    public String getCarManufacture() { return carManufacture; }
    public void setCarManufacture(String carManufacture) { this.carManufacture = carManufacture; }
    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }
}