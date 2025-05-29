package com.autoxpress.usedcarsalesportal.service;

import com.autoxpress.usedcarsalesportal.dto.AppointmentDto;
import com.autoxpress.usedcarsalesportal.model.Appointment;
import com.autoxpress.usedcarsalesportal.model.Car;
import com.autoxpress.usedcarsalesportal.model.User;
import com.autoxpress.usedcarsalesportal.repository.AppointmentRepository;
import com.autoxpress.usedcarsalesportal.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CarRepository carRepository;

    public Appointment bookAppointment(AppointmentDto dto, User user) {
        try {
            logger.info("Booking appointment for user: {}, carId: {}, date: {}", user.getUsername(), dto.getCarId(), dto.getAppointmentDate());
            Appointment appointment = new Appointment();
            appointment.setUser(user);
            Car car = carRepository.findById(dto.getCarId()).orElseThrow(() -> {
                logger.error("Car not found for ID: {}", dto.getCarId());
                return new RuntimeException("Car not found");
            });
            appointment.setCar(car);
            appointment.setAppointmentDate(dto.getAppointmentDate());
            appointment.setStatus("PENDING");
            Appointment savedAppointment = appointmentRepository.save(appointment);
            logger.info("Appointment booked successfully with ID: {}", savedAppointment.getId());
            return savedAppointment;
        } catch (Exception e) {
            logger.error("Error booking appointment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to book appointment: " + e.getMessage(), e);
        }
    }

    public void approveAppointment(Long appointmentId) {
        setAppointmentStatus(appointmentId, "APPROVED");
    }

    public void denyAppointment(Long appointmentId) {
        setAppointmentStatus(appointmentId, "DENIED");
    }

    public void setPendingAppointment(Long appointmentId) {
        setAppointmentStatus(appointmentId, "PENDING");
    }

    public void setAppointmentStatus(Long appointmentId, String status) {
        logger.info("Setting appointment ID: {} to status: {}", appointmentId, status);
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> {
            logger.error("Appointment not found for ID: {}", appointmentId);
            return new RuntimeException("Appointment not found");
        });
        if (!List.of("PENDING", "APPROVED", "DENIED").contains(status)) {
            logger.error("Invalid status: {}", status);
            throw new RuntimeException("Invalid status: " + status);
        }
        appointment.setStatus(status);
        appointmentRepository.save(appointment);
        logger.info("Appointment ID: {} set to status: {}", appointmentId, status);
    }

    public List<Appointment> getPendingAppointments() {
        logger.info("Fetching pending appointments");
        return appointmentRepository.findByStatus("PENDING");
    }

    public List<Appointment> getAllAppointments() {
        logger.info("Fetching all appointments (PENDING, APPROVED, DENIED)");
        return appointmentRepository.findByStatusIn(List.of("PENDING", "APPROVED", "DENIED"));
    }
}