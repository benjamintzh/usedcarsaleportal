package com.autoxpress.usedcarsalesportal.repository;

import com.autoxpress.usedcarsalesportal.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByCarId(Long carId);
    List<Appointment> findByStatus(String status);
    List<Appointment> findByStatusIn(List<String> statuses);
}