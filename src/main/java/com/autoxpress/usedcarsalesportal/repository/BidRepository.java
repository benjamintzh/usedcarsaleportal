package com.autoxpress.usedcarsalesportal.repository;

import com.autoxpress.usedcarsalesportal.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByCarId(Long carId);
    List<Bid> findByStatus(String status);
    List<Bid> findByStatusIn(List<String> statuses);
}