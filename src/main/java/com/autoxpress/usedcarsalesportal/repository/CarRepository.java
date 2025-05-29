package com.autoxpress.usedcarsalesportal.repository;

import com.autoxpress.usedcarsalesportal.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByManufactureContainingIgnoreCaseAndModelContainingIgnoreCaseAndRegistrationYearBetweenAndPriceBetween(
        String manufacture, String model, int yearMin, int yearMax, double priceMin, double priceMax);
    
    List<Car> findByActiveTrue();
    
    List<Car> findByUserId(Long userId);
    
    List<Car> findByManufactureContainingIgnoreCaseAndModelContainingIgnoreCaseAndRegistrationYearBetweenAndPriceBetweenAndActiveTrue(
            String manufacture, String model, int yearMin, int yearMax, double priceMin, double priceMax);
    
    List<Car> findAll();
    
    @Query("SELECT DISTINCT c.manufacture FROM Car c ORDER BY c.manufacture")
    List<String> findDistinctManufacturers();

    // Updated query to find cars with approved bids
    @Query("SELECT c FROM Car c JOIN Bid b ON c.id = b.car.id " +
           "WHERE c.active = true AND b.status = 'ACCEPTED'")
    List<Car> findAllActiveWithApprovedBid();

    // Updated query for search with approved bids
    @Query("SELECT c FROM Car c JOIN Bid b ON c.id = b.car.id " +
           "WHERE c.active = true AND b.status = 'ACCEPTED' " +
           "AND lower(c.manufacture) LIKE lower(concat('%', :manufacture, '%')) " +
           "AND lower(c.model) LIKE lower(concat('%', :model, '%')) " +
           "AND c.registrationYear BETWEEN :yearMin AND :yearMax " +
           "AND c.price BETWEEN :priceMin AND :priceMax")
    List<Car> findByManufactureContainingIgnoreCaseAndModelContainingIgnoreCaseAndRegistrationYearBetweenAndPriceBetweenAndActiveWithApprovedBid(
            String manufacture, String model, int yearMin, int yearMax, double priceMin, double priceMax);
}