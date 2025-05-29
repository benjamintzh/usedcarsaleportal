package com.autoxpress.usedcarsalesportal.repository;

import com.autoxpress.usedcarsalesportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT DISTINCT u.username FROM Appointment a JOIN a.user u ORDER BY u.username")
    List<String> findDistinctUsernamesWithAppointments();
}